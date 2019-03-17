package routers;

import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;
import services.MessageHistoryService;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.io.IOException;

@Path("/traces")
public class TracesRouter {

    private MessageHistoryService messageHistoryService;

    public TracesRouter(MessageHistoryService messageHistoryService) {
        this.messageHistoryService = messageHistoryService;
    }

    @POST
    @Path("/list")
    @Consumes("application/json")
    @Produces("application/json")
    public ListDTO getTracesList(PaginationParams paginationParams) throws IOException {
        ListDTO listInfo = messageHistoryService.getTracesList(paginationParams);
        return listInfo;
    }

    @POST
    @Path("/:id/list")
    @Consumes("application/json")
    @Produces("application/json")
    public ListDTO getTraceMessagesList(@PathParam("id") @NotNull Integer id, PaginationParams paginationParams) throws IOException {
        ListDTO listInfo = messageHistoryService.getTraceMessages(id, paginationParams);
        return listInfo;
    }

}
