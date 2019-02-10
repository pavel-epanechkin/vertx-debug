package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.LimitParams;
import dao.history.processed.MessageHistoryDaoImpl;
import dao.history.raw.RawHistoryDao;
import dao.history.raw.RawHistoryDaoImpl;
import domain.MessageInfo;
import dao.history.processed.MessagesHistoryDao;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHistoryService {

    private MessagesHistoryDao processedHistoryDao;

    private HistoryProcessingService historyProcessingService;

    public MessageHistoryService(HistoryProcessingService historyProcessingService) {
        this.historyProcessingService = historyProcessingService;
    }

    public void connectHistoryDb(String rawHistoryFilePath) throws Exception {
        if (processedHistoryDao != null) {
            processedHistoryDao.close();
        }

        processedHistoryDao = historyProcessingService.startProcessingRawHistory(rawHistoryFilePath);
    }

    public void disconnectHistoryDb() throws IOException {
        if (processedHistoryDao != null) {
            processedHistoryDao.close();
            processedHistoryDao = null;
        }
    }

    public double getProcessingProgress() throws IOException {
        return historyProcessingService.getProcessingProgress();
    }

    public void stopProcessing() throws IOException {
        historyProcessingService.stopProcessing();
    }

    public MessageInfoDTO getMessageInfo(Integer id) throws IOException {
        MessageInfo messageInfo = processedHistoryDao.getByRecordId(id);
        return prepareMessageInfoDto(messageInfo);
    }

    public ListDTO getList(PaginationParams paginationParams) throws IOException {
        int pageNumber = paginationParams.getPageNumber();
        int itemsOnPage = paginationParams.getItemsOnPage();
        int offset = pageNumber*itemsOnPage;

        List<MessageInfo> messageInfos = processedHistoryDao.getByLikeFilterWithLimit(
                paginationParams.getFilter(),
                new LimitParams(offset, itemsOnPage),
                paginationParams.getStartDate(),
                paginationParams.getEndDate()
        );
        List<MessageInfoDTO> resultData = messageInfos.stream()
                .map(this::prepareMessageInfoDto)
                .collect(Collectors.toList());

        int totalCount = processedHistoryDao.getCountByLikeFilter(
                paginationParams.getFilter(),
                paginationParams.getStartDate(),
                paginationParams.getEndDate()
        );

        ListDTO resultDto = new ListDTO(pageNumber, itemsOnPage, totalCount,
                paginationParams.getFilter(), resultData);

        return resultDto;
    }

    private MessageInfoDTO prepareMessageInfoDto(MessageInfo messageInfo) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(messageInfo, MessageInfoDTO.class);
    }
}
