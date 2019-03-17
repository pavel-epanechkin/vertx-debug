package services;

import dao.history.processed.interfaces.ProcessedDaoFactory;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;

import java.io.IOException;

public class MessageHistoryService {

    private ProcessedDaoFactory daoManager;

    private HistoryProcessingService historyProcessingService;

    private MessagesService messagesService;

    private TracesService tracesService;

    public MessageHistoryService(HistoryProcessingService historyProcessingService) {
        this.historyProcessingService = historyProcessingService;
    }

    public void connectHistoryDb(String rawHistoryFilePath) throws Exception {
        if (daoManager != null) {
            daoManager.close();
        }

        try {
            daoManager = historyProcessingService.startProcessingRawHistory(rawHistoryFilePath);
            messagesService = new MessagesService(daoManager.getMessagesDao());
            tracesService = new TracesService(daoManager.getTracesDao());
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void disconnectHistoryDb() throws IOException {
        if (daoManager != null) {
            daoManager.close();
            daoManager = null;
        }
    }

    public double getProcessingProgress() throws IOException {
        return historyProcessingService.getProcessingProgress();
    }

    public void stopProcessing() throws IOException {
        historyProcessingService.stopProcessing();
    }

    public MessageInfoDTO getMessageInfo(Integer id) throws IOException {
        return messagesService.getMessageInfo(id);
    }

    public ListDTO getMessagesList(PaginationParams paginationParams) throws IOException {
        return messagesService.getMessagesList(paginationParams);
    }

    public ListDTO getTracesList(PaginationParams paginationParams) throws IOException {
        return tracesService.getTracesList(paginationParams);
    }

    public ListDTO getTraceMessages(Integer traceId, PaginationParams paginationParams) throws IOException {
        return messagesService.getTraceMessages(traceId, paginationParams);
    }

    public ListDTO getTracesForMessage(Integer messageId, PaginationParams paginationParams) throws IOException {
        return tracesService.getTracesForMessage(messageId, paginationParams);
    }

}
