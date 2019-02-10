package services;

import com.zandero.rest.RestRouter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import routers.MessageHistoryRouter;

public class MainVerticle extends AbstractVerticle {

    private static final int SERVER_PORT = 9999;

    private HttpServer httpServer;

    private Router messageHistoryRouter;

    private MessageHistoryService messageHistoryService;

    private HistoryProcessingService historyProcessingService;

    public void start(Future<Void> startFuture) {
        initializeServices();
        registerRouters();
        createHttpServer();
        startFuture.complete();
    }

    public void stop(Future<Void> stopFuture) {
        httpServer.close();
        stopFuture.complete();
    }

    private void initializeServices() {
        historyProcessingService = new HistoryProcessingService();
        messageHistoryService = new MessageHistoryService(historyProcessingService);
    }

    private void registerRouters() {
        messageHistoryRouter = RestRouter.register(vertx, new MessageHistoryRouter(messageHistoryService));
    }

    private void createHttpServer() {
        httpServer = vertx.createHttpServer()
            .requestHandler(messageHistoryRouter)
            .listen(SERVER_PORT);
    }
}
