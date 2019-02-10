package services;

import dao.history.processed.MessageHistoryDaoImpl;
import dao.history.processed.MessagesHistoryDao;
import dao.history.raw.RawHistoryDao;
import dao.history.raw.RawHistoryDaoImpl;
import dao.history.raw.RawMessageAction;
import domain.MessageInfo;
import domain.RawMessageInfo;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class HistoryProcessingService  {

    private Thread processingThread;

    private String rawHistoryFilePath;

    private RawHistoryDaoImpl currentRawHistoryDao;

    private MessagesHistoryDao currentMessagesHistoryDao;

    private AtomicBoolean processing = new AtomicBoolean(false);

    private AtomicInteger processedCount = new AtomicInteger(0);

    private int recordsToProcessCount;

    private final String RECEIVED_MESSAGE_POSTFIX = "-r";

    public MessagesHistoryDao startProcessingRawHistory(String rawHistoryFilePath) throws Exception {
        if (processing.get()) {
            if (rawHistoryFilePath.equals(this.rawHistoryFilePath))
                return currentMessagesHistoryDao;
            else
                throw new Exception("Some DB is being processed. Stop it before start processing another DB.");
        }

        currentRawHistoryDao = new RawHistoryDaoImpl(rawHistoryFilePath);
        currentMessagesHistoryDao = new MessageHistoryDaoImpl(rawHistoryFilePath + File.separator + "processed");

        recordsToProcessCount = currentRawHistoryDao.getRecordsCount();

        processingThread = new Thread(this::startProcessingRawHistory);
        processingThread.start();

        return currentMessagesHistoryDao;
    }

    private void startProcessingRawHistory() {
        processing.set(true);
        processedCount.set(0);

        currentRawHistoryDao.foreach(rawMessageInfo -> {
            processRawRecord(rawMessageInfo);
            processedCount.incrementAndGet();
            return !processing.get();
        });
    }

    private void processRawRecord(RawMessageInfo rawMessageInfo) {
        if (rawMessageInfo.getType().equals("sent")) {
            MessageInfo messageInfo = getMessageInfoFromRawRecord(rawMessageInfo);
            currentMessagesHistoryDao.insert(messageInfo);
        }
        else {
            MessageInfo messageInfo = currentMessagesHistoryDao.getByMessageId(rawMessageInfo.getMessageId());
            if (messageInfo != null) {
                messageInfo.setReceived(true);
                messageInfo.setReceivedTime(rawMessageInfo.getTimestamp());
                currentMessagesHistoryDao.update(messageInfo);
            }
        }
    }

    private MessageInfo getMessageInfoFromRawRecord(RawMessageInfo rawMessageInfo) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setLabel(rawMessageInfo.getLabel());
        messageInfo.setMessageId(rawMessageInfo.getMessageId());
        messageInfo.setPrevMessageId(rawMessageInfo.getPrevMessageId());
        messageInfo.setSentTime(rawMessageInfo.getTimestamp());
        messageInfo.setBody(rawMessageInfo.getBody());
        messageInfo.setHeaders(rawMessageInfo.getHeaders());
        messageInfo.setTargetAddress(rawMessageInfo.getTargetAddress());
        messageInfo.setReplyAddress(rawMessageInfo.getReplyAddress());
        messageInfo.setReceived(false);

        return messageInfo;
    }

    public void stopProcessing() {
        processing.set(false);
        try {
            processingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getProcessingProgress() {
        if (processing.get()) {
            if (recordsToProcessCount == 0)
                return 1;
            return ((double) processedCount.get()) / ((double) recordsToProcessCount);
        }
        return 0;
    }

}
