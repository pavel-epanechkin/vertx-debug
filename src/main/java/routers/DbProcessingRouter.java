package routers;

import dto.requests.ConnectDBRequestDTO;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;
import services.MessageHistoryService;
import dto.requests.PaginationParams;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.io.IOException;

@Path("/db")
public class DbProcessingRouter {

    private MessageHistoryService messageHistoryService;

    public DbProcessingRouter(MessageHistoryService messageHistoryService) {
        this.messageHistoryService = messageHistoryService;
    }

    @POST
    @Path("/connect")
    @Consumes("application/json")
    public void connectHistoryDb(ConnectDBRequestDTO dto) throws Exception {
        messageHistoryService.connectHistoryDb(dto.getFilePath());
    }

    @POST
    @Path("/disconnect")
    public void disconnectHistoryDb() throws IOException {
        messageHistoryService.disconnectHistoryDb();
    }

    @GET
    @Path("/processingProgress")
    public double getProcessingProgress() throws IOException {
        return messageHistoryService.getProcessingProgress();
    }

    @GET
    @Path("/stopProcessing")
    public void stopProcessing() throws IOException {
        messageHistoryService.stopProcessing();
    }

}
