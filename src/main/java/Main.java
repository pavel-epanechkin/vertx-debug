import io.vertx.core.Vertx;
import services.MainVerticle;

public class Main {

    public static void main(String args[]){
        Vertx vertx = Vertx.vertx();
        MainVerticle mainVerticle = new MainVerticle();
        vertx.deployVerticle(mainVerticle);
    }
}
