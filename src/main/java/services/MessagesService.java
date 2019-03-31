package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.LimitParams;
import dao.history.processed.MessagesDao;
import database.generated.public_.tables.pojos.Message;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
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
        MessageInfoDTO messageInfoDTO = new MessageInfoDTO();
        messageInfoDTO.setReceived(message.getReceived());
        messageInfoDTO.setBody(message.getBody());
        messageInfoDTO.setLabel(message.getLabel());
        messageInfoDTO.setId(message.getId());
        messageInfoDTO.setMessageId(message.getMessageId());
        messageInfoDTO.setPrevMessageId(message.getPrevMessageId());
        messageInfoDTO.setTargetAddress(message.getTargetAddress());
        messageInfoDTO.setReplyAddress(message.getReplyAddress());
        messageInfoDTO.setSentTime(message.getSentTime().toString());
        messageInfoDTO.setReceivedTime(message.getReceivedTime() == null ? "" : message.getReceivedTime().toString());

        List<Map<String, Object>> headers = new ArrayList<>();
        if (message.getHeaders() != null && !message.getHeaders().isEmpty()) {
            headers = prepareHeaders(message.getHeaders());
        }
        messageInfoDTO.setHeaders(headers);

        return messageInfoDTO;
    }

    private List<Map<String, Object>> prepareHeaders(String jsonHeaders) {
        List<Map<String, Object>> resultHeaders = new ArrayList<>();

        Object headersObject = Utils.getObjectFromJsonString(jsonHeaders, List.class);
        if (headersObject != null) {
            List<Map<String, Object>> tmpHeaders = ((List<Map<String, Object>>) headersObject);
            for (Map<String, Object> headerMap : tmpHeaders) {
                for (Map.Entry<String, Object> header : headerMap.entrySet()) {
                    Map<String, Object> headerDescription = new HashMap<>();
                    headerDescription.put("key", header.getKey());
                    headerDescription.put("value", header.getValue());
                    resultHeaders.add(headerDescription);
                }
            }
        }

        return resultHeaders;
    }
}
