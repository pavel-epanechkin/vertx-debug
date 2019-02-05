package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.LimitParams;
import dao.MessageHistoryDaoImpl;
import dao.MessageInfo;
import dao.MessagesHistoryDao;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHistoryService {

    private MessagesHistoryDao currentHistoryDao;

    public void connectHistoryDb(String historyFilePath) throws IOException {
        if (currentHistoryDao != null) {
            currentHistoryDao.close();
        }
        currentHistoryDao = new MessageHistoryDaoImpl(historyFilePath);
    }

    public void disconnectHistoryDb() throws IOException {
        if (currentHistoryDao != null) {
            currentHistoryDao.close();
            currentHistoryDao = null;
        }
    }

    public MessageInfoDTO getMessageInfo(Integer id) throws IOException {
        MessageInfo messageInfo = currentHistoryDao.getByRecordId(id);
        return prepareMessageInfoDto(messageInfo);
    }

    public ListDTO getList(PaginationParams paginationParams) throws IOException {
        int pageNumber = paginationParams.getPageNumber();
        int itemsOnPage = paginationParams.getItemsOnPage();
        int offset = pageNumber*itemsOnPage;

        List<MessageInfo> messageInfos = currentHistoryDao.getByLikeFilterWithLimit(
                paginationParams.getFilter(),
                new LimitParams(offset, itemsOnPage),
                paginationParams.getStartDate(),
                paginationParams.getEndDate()
        );
        List<MessageInfoDTO> resultData = messageInfos.stream()
                .map(this::prepareMessageInfoDto)
                .collect(Collectors.toList());

        int totalCount = currentHistoryDao.getCountByLikeFilter(
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
