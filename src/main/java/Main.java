import io.vertx.core.Vertx;
import services.DebuggingService;

public class Main {

    public static void main(String args[]){
        Vertx vertx = Vertx.vertx();
        DebuggingService debuggingService = new DebuggingService();
        vertx.deployVerticle(debuggingService);
    }
}
