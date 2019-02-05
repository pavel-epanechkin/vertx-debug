package routers;

import dao.MessageInfo;
import dto.requests.ConnectDBRequestDTO;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;
import services.MessageHistoryService;
import dto.requests.PaginationParams;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.io.IOException;

@Path("/history")
public class MessageHistoryRouter {

    private MessageHistoryService messageHistoryService;

    public MessageHistoryRouter(MessageHistoryService messageHistoryService) {
        this.messageHistoryService = messageHistoryService;
    }

    @POST
    @Path("/connect")
    @Consumes("application/json")
    public void connectHistoryDb(ConnectDBRequestDTO dto) throws IOException {
        messageHistoryService.connectHistoryDb(dto.getFilePath());
    }

    @POST
    @Path("/disconnect")
    public void disconnectHistoryDb() throws IOException {
        messageHistoryService.disconnectHistoryDb();
    }

    @GET
    @Path("/messages/:id")
    @Produces("application/json")
    public MessageInfoDTO getMessageInfo(@PathParam("id") @NotNull Integer id) throws IOException {
        MessageInfoDTO messageInfoDTO = messageHistoryService.getMessageInfo(id);
        return messageInfoDTO;
    }

    @POST
    @Path("/messages/list")
    @Consumes("application/json")
    @Produces("application/json")
    public ListDTO getMessagesList(PaginationParams paginationParams) throws IOException {
        ListDTO listInfo = messageHistoryService.getList(paginationParams);
        return listInfo;
    }
}
