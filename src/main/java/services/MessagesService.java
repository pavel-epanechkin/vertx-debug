package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.LimitParams;
import dao.history.processed.MessagesDao;
import database.generated.public_.tables.pojos.Message;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessagesService {

    private MessagesDao messagesDao;

    public MessagesService(MessagesDao messagesDao) {
        this.messagesDao = messagesDao;
    }

    public MessageInfoDTO getMessageInfo(Integer id) throws IOException {
        Message message = messagesDao.findById(id);
        return prepareMessageInfoDto(message);
    }

    public ListDTO getMessagesList(PaginationParams paginationParams) throws IOException {
        int pageNumber = paginationParams.getPageNumber();
        int itemsOnPage = paginationParams.getItemsOnPage();
        int offset = pageNumber*itemsOnPage;

        List<Message> messages = messagesDao.getByLikeFilterWithLimit(
                paginationParams.getFilter(),
                new LimitParams(offset, itemsOnPage)
        );
        List<MessageInfoDTO> resultData = messages.stream()
                .map(this::prepareMessageInfoDto)
                .collect(Collectors.toList());

        int totalCount = messagesDao.getCountByLikeFilter(
                paginationParams.getFilter()
        );

        ListDTO resultDto = new ListDTO(pageNumber, itemsOnPage, totalCount,
                paginationParams.getFilter(), resultData);

        return resultDto;
    }

    public ListDTO getTraceMessages(Integer traceId, PaginationParams paginationParams) throws IOException {
        int pageNumber = paginationParams.getPageNumber();
        int itemsOnPage = paginationParams.getItemsOnPage();
        int offset = pageNumber*itemsOnPage;

        List<Message> messages = messagesDao.getMessagesByTraceId(
                traceId,
                paginationParams.getFilter(),
                new LimitParams(offset, itemsOnPage)
        );
        List<MessageInfoDTO> resultData = messages.stream()
                .map(this::prepareMessageInfoDto)
                .collect(Collectors.toList());

        int totalCount = messagesDao.getMessagesCountByTraceId(traceId);

        ListDTO resultDto = new ListDTO(pageNumber, itemsOnPage, totalCount,
                paginationParams.getFilter(), resultData);

        return resultDto;
    }

    private MessageInfoDTO prepareMessageInfoDto(Message message) {
        ObjectMapper mapper = new ObjectMapper();
        MessageInfoDTO messageInfoDTO = mapper.convertValue(message, MessageInfoDTO.class);
        messageInfoDTO.setSentTime(message.getSentTime().toString());

        Map<String, Object> headers = new HashMap<>();
        if (message.getHeaders() != null && !message.getHeaders().isEmpty()) {

        }

        messageInfoDTO.setHeaders(headers);

        return messageInfoDTO;
    }
}
