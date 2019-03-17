package services;

import com.zandero.rest.RestBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import routers.DbProcessingRouter;
import routers.MessagesRouter;
import routers.TracesRouter;

import java.util.HashSet;
import java.util.Set;

public class MainVerticle extends AbstractVerticle {

    private static final int SERVER_PORT = 9999;

    private HttpServer httpServer;

    private Router router;

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
        Router router = Router.router(vertx);

        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");

        HttpMethod[] allowedMethods = new HttpMethod[] {
                HttpMethod.GET, HttpMethod.POST,
                HttpMethod.DELETE, HttpMethod.PATCH,
                HttpMethod.OPTIONS, HttpMethod.PUT
        };

        this.router = new RestBuilder(vertx)
                .enableCors("*", false, 1728000, allowedHeaders, allowedMethods)
                .register(new DbProcessingRouter(messageHistoryService))
                .register(new MessagesRouter(messageHistoryService))
                .register(new TracesRouter(messageHistoryService))
                .build();

    }

    private void createHttpServer() {
        httpServer = vertx.createHttpServer()
            .requestHandler(router)
            .listen(SERVER_PORT);
    }
}
