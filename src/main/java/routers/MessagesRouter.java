package routers;

import dto.requests.ConnectDBRequestDTO;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;
import services.MessageHistoryService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.io.IOException;

@Path("/messages")
public class MessagesRouter {

    private MessageHistoryService messageHistoryService;

    public MessagesRouter(MessageHistoryService messageHistoryService) {
        this.messageHistoryService = messageHistoryService;
    }

    @GET
    @Path("/:id")
    @Produces("application/json")
    public MessageInfoDTO getMessageInfo(@PathParam("id") @NotNull Integer id) throws IOException {
        MessageInfoDTO messageInfoDTO = messageHistoryService.getMessageInfo(id);
        return messageInfoDTO;
    }

    @POST
    @Path("/list")
    @Consumes("application/json")
    @Produces("application/json")
    public ListDTO getMessagesList(PaginationParams paginationParams) throws IOException {
        try {
            ListDTO listInfo = messageHistoryService.getMessagesList(paginationParams);
            return listInfo;
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    @POST
    @Path("/:id/relatedTraces")
    @Consumes("application/json")
    @Produces("application/json")
    public ListDTO getMessageRelatedTraces(@PathParam("id") @NotNull Integer id, PaginationParams paginationParams) throws IOException {
        ListDTO listInfo = messageHistoryService.getTracesForMessage(id, paginationParams);
        return listInfo;
    }
}
