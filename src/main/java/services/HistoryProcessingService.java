package services;

import dao.history.processed.ProcessedDaoFactoryImpl;
import dao.history.processed.interfaces.ProcessedDaoFactory;
import dao.history.raw.*;
import dao.history.raw.interfaces.RawDaoFactory;
import database.generated.public_.Tables;
import database.generated.public_.tables.pojos.*;
import domain.*;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class HistoryProcessingService  {

    private Thread historyProcessingThread;

    private Thread messagesConvertingThread;

    private Thread historyAnalysingThread;

    private String rawHistoryFilePath;

    private ProcessedDaoFactory processedDaoFactory;

    private RawDaoFactory rawDaoFactory;

    private AtomicBoolean processing = new AtomicBoolean(false);

    private AtomicBoolean analysing = new AtomicBoolean(false);

    private AtomicBoolean fullyProcessed = new AtomicBoolean(false);

    private AtomicInteger processedCount = new AtomicInteger(0);

    private AtomicInteger analyzedCount = new AtomicInteger(0);

    private int recordsToProcessCount;


    public ProcessedDaoFactory startProcessingRawHistory(String rawHistoryFilePath) throws Exception {
        if (processing.get()) {
            if (rawHistoryFilePath.equals(this.rawHistoryFilePath))
                return processedDaoFactory;
            else
                throw new Exception("Some DB is being processed. Stop it before start processing another DB.");
        }

        String processedDBPath = rawHistoryFilePath + File.separator + "processed";
        boolean isProcessedDbExists = Files.exists(Paths.get(processedDBPath + ".mv.db"));

        rawDaoFactory = new RawDaoFactoryImpl(rawHistoryFilePath);
        processedDaoFactory = new ProcessedDaoFactoryImpl(processedDBPath);
        recordsToProcessCount = rawDaoFactory.getMessageHistoryDao().getRecordsCount();

        if (!isProcessedDbExists) {
            historyProcessingThread = new Thread(this::startProcessingRawHistory);
            historyProcessingThread.start();
        }
        else
            fullyProcessed.set(true);

        return processedDaoFactory;
    }


    private void startProcessingRawHistory() {
        fullyProcessed.set(false);
        messagesConvertingThread = new Thread(this::convertRawMessages);
        messagesConvertingThread.start();

        historyAnalysingThread = new Thread(this::analyseRawHistory);
        historyAnalysingThread.start();

        try {
            historyAnalysingThread.join();
            messagesConvertingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        convertAnalysis();
        fullyProcessed.set(true);
    }

    private void convertRawMessages() {
        processing.set(true);
        processedCount.set(0);

        rawDaoFactory.getMessageHistoryDao().foreachSent(pair -> {
            processRawRecord(pair.getValue());
            processedCount.incrementAndGet();
            return !processing.get();
        });
    }

    private void analyseRawHistory() {
        analysing.set(true);
        analyzedCount.set(0);

        calcullateMessagesChildsCount();
        createRawTraceUnitsAndSpans();
        createRawTraceParts();
    }

    private void calcullateMessagesChildsCount() {
        rawDaoFactory.getMessageHistoryDao().foreachSent(pair -> {
            updateChildsCount(pair.getValue().getPrevMessageId());
            return !analysing.get();
        });
    }

    private void createRawTraceUnitsAndSpans() {
        rawDaoFactory.getMessageHistoryDao().foreachSent(pair -> {
            analyseRawRecord(pair.getKey(), pair.getValue());
            analyzedCount.incrementAndGet();
            return !analysing.get();
        });
    }

    private void createRawTraceParts() {
        rawDaoFactory.getSpansDao().foreach(pair -> {
            Integer spanId = pair.getKey();
            RawSpan span = pair.getValue();
            if (span.getChildSpansCount() == 0)
                buildTrace(spanId, span);
            return !analysing.get();
        });
    }

    private void buildTrace(Integer spanId, RawSpan span) {
        Stack<Pair<Integer, RawSpan>> traverseStack = new Stack<>();
        traverseStack.push(new Pair<>(spanId, span));
        RawSpan currentSpan = span;

        while (currentSpan.getPrevSpanId() != -1) {
            Integer prevSpanId = currentSpan.getPrevSpanId();
            currentSpan = rawDaoFactory.getSpansDao().get(prevSpanId);
            traverseStack.push(new Pair<>(prevSpanId, currentSpan));
        }

        int traceId = rawDaoFactory.getTracesDao().insert(new RawTrace());
        int tracePartOrderNum = 1;
        while (!traverseStack.empty()) {
            Pair<Integer,RawSpan> currentPair = traverseStack.pop();
            int tracePartId = rawDaoFactory.getTracePartsDao().insert(new RawTracePart(traceId, tracePartOrderNum++, currentPair.getKey()));
        }

    }

    private void processRawRecord(RawMessageInfo rawMessageInfo) {
        Message message = getMessageFromRawRecord(rawMessageInfo);

        RawMessageInfo received = rawDaoFactory.getMessageHistoryDao().getReceivedMessageById(message.getMessageId());
        if (received != null) {
            message.setReceived(true);
            message.setReceivedTime(new Timestamp(rawMessageInfo.getTimestamp()));
        }

        processedDaoFactory.getMessagesDao().insert(message);
    }

    private void analyseRawRecord(Integer messageInfoId, RawMessageInfo rawMessageInfo) {
        RawSpanDao rawSpanDao = rawDaoFactory.getSpansDao();
        RawTraceUnitDao rawTraceUnitDao = rawDaoFactory.getTraceUnitsDao();
        RawHistoryDao rawHistoryDao = rawDaoFactory.getMessageHistoryDao();

        try {
            String prevMessageId = rawMessageInfo.getPrevMessageId();
            if (prevMessageId == null || prevMessageId.isEmpty()) {
                RawSpan newSpan = new RawSpan(-1, 0);
                newSpan.setFirstMessageLabel(rawMessageInfo.getLabel());
                newSpan.setStartTime(rawMessageInfo.getTimestamp());
                int spanId = rawSpanDao.insert(newSpan);
                rawTraceUnitDao.insert(rawMessageInfo.getMessageId(), new RawTraceUnit(spanId, 1));
            }
            else {
                int prevMessageChildsCount = rawHistoryDao.getMessageChildsCount(prevMessageId);
                RawTraceUnit prevMessageTraceUnit = rawTraceUnitDao.get(prevMessageId);
                int prevSpanId = prevMessageTraceUnit.getSpanId();
                RawSpan prevMessageSpan = rawSpanDao.get(prevSpanId);

                if (prevMessageChildsCount <= 1) {
                    rawTraceUnitDao.insert(rawMessageInfo.getMessageId(),
                            new RawTraceUnit(prevMessageTraceUnit.getSpanId(), prevMessageTraceUnit.getOrderNumber() + 1));

                    prevMessageSpan.setLastMessageLabel(rawMessageInfo.getLabel());
                    prevMessageSpan.setEndTime(rawMessageInfo.getTimestamp());
                }
                else {
                    RawSpan newSpan = new RawSpan(prevSpanId, 0);
                    newSpan.setFirstMessageLabel(rawMessageInfo.getLabel());
                    newSpan.setStartTime(rawMessageInfo.getTimestamp());
                    int newSpanId = rawSpanDao.insert(newSpan);
                    rawTraceUnitDao.insert(rawMessageInfo.getMessageId(),new RawTraceUnit(newSpanId, 1));

                    prevMessageSpan.setChildSpansCount(prevMessageSpan.getChildSpansCount() + 1);

                    if (prevMessageSpan.getLastMessageLabel() == null) {
                        RawMessageInfo prevMessage = rawHistoryDao.getSentMessageByMessageId(prevMessageId);
                        prevMessageSpan.setLastMessageLabel(prevMessage.getLabel());
                        prevMessageSpan.setEndTime(prevMessage.getTimestamp());
                    }
                }
                rawSpanDao.update(prevSpanId, prevMessageSpan);
            }
        }
        catch (Exception err) {
            System.out.println("Id: " + messageInfoId);
            System.out.println("Prev id: " + rawMessageInfo.getPrevMessageId());
        }
    }

    private void updateChildsCount(String messageId) {
        RawHistoryDao rawHistoryDao = rawDaoFactory.getMessageHistoryDao();

        if (messageId != null) {
            int childsCount = rawHistoryDao.getMessageChildsCount(messageId);
            rawHistoryDao.updateMessageChildsCount(messageId, ++childsCount);
        }
    }

    private void convertAnalysis() {
        rawDaoFactory.getTracesDao().foreach(pair -> {
            Trace trace = getTraceFromRaw(pair.getValue());
            processedDaoFactory.getTracesDao().insert(trace);
            return !processing.get();
        });

        rawDaoFactory.getSpansDao().foreach(pair -> {
            Span span = getSpanFromRaw(pair.getValue());
            processedDaoFactory.getSpansDao().insert(span);
            return !processing.get();
        });

        rawDaoFactory.getTracePartsDao().foreach(pair -> {
            TracePart tracePart = getTracePartFromRaw(pair.getValue());
            processedDaoFactory.getTracePartsDao().insert(tracePart);
            return !processing.get();
        });

        rawDaoFactory.getTraceUnitsDao().foreach(pair -> {
            TraceUnit traceUnit = getTraceUnitFromRaw(pair.getKey(), pair.getValue());
            processedDaoFactory.getTraceUnitsDao().insert(traceUnit);
            return !processing.get();
        });
    }

    private Trace getTraceFromRaw(RawTrace rawTrace) {
        Trace trace = new Trace();
        return trace;
    }

    private Span getSpanFromRaw(RawSpan rawSpan) {
        Span span = new Span();
        span.setFirstMessageLabel(rawSpan.getFirstMessageLabel());
        span.setLastMessageLabel(rawSpan.getLastMessageLabel());
        span.setStartTime(new Timestamp(rawSpan.getStartTime()));
        span.setEndTime(new Timestamp(rawSpan.getEndTime()));
        return span;
    }

    private TracePart getTracePartFromRaw(RawTracePart rawTracePart) {
        TracePart tracePart = new TracePart();
        tracePart.setTraceId(rawTracePart.getTraceId());
        tracePart.setSpanId(rawTracePart.getSpanId());
        tracePart.setOrderNumber(rawTracePart.getOrderNumber());

        return tracePart;
    }

    private TraceUnit getTraceUnitFromRaw(String rawTraceUnitId, RawTraceUnit rawTraceUnit) {
        Message message = processedDaoFactory.getMessagesDao().fetchOne(Tables.MESSAGE.MESSAGE_ID, rawTraceUnitId);

        TraceUnit traceUnit = new TraceUnit();
        traceUnit.setMessageId(message.getId());
        traceUnit.setSpanId(rawTraceUnit.getSpanId());
        traceUnit.setOrderNum(rawTraceUnit.getOrderNumber());

        return traceUnit;
    }

    private Message getMessageFromRawRecord(RawMessageInfo rawMessageInfo) {
        Message message = new Message();
        message.setLabel(rawMessageInfo.getLabel());
        message.setMessageId(rawMessageInfo.getMessageId());
        message.setPrevMessageId(rawMessageInfo.getPrevMessageId());
        message.setSentTime(new Timestamp(rawMessageInfo.getTimestamp()));
        message.setBody(rawMessageInfo.getBody());
        message.setHeaders(rawMessageInfo.getHeaders());
        message.setTargetAddress(rawMessageInfo.getTargetAddress());
        message.setReplyAddress(rawMessageInfo.getReplyAddress());
        message.setReceived(false);

        return message;
    }

    public void stopProcessing() {
        processing.set(false);
        analysing.set(false);
        try {
            historyProcessingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getProcessingProgress() {
        if (!fullyProcessed.get()) {
            if (recordsToProcessCount == 0)
                return 1;
            return ((double) processedCount.get()) / ((double) recordsToProcessCount);
        }
        return 1;
    }

}
