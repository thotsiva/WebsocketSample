package com.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

//    @Override
//    public void start(Promise<Void> startPromise) throws Exception {
//        System.out.println("This is the MainVerticle of the project");
//
//        initializeServerVerticle()
//            .compose(this::initializeUserVerticle)
//            .onComplete( handler -> {
//                if(handler.succeeded()){
//                    System.out.println("Main Verticle Deployment Successful");
//                    startPromise.complete();
//                }else{
//                    System.out.println("Main Verticle Deployment Failed");
//                    startPromise.fail(handler.cause().getMessage());
//                }
//            });
//    }
//
//    public Future<Void> initializeServerVerticle(){
//
//        Verticle serverVerticle = new ServerVerticle();
//        vertx.deployVerticle(serverVerticle)
//                .compose( string -> {
//                    System.out.println("[initializeServerVerticle] :"+string);
//                    return Future.future(p->p.complete());
//                });
//
//        return Future.future(p->p.complete());
//    }
//
//    public Future<Void> initializeUserVerticle(Void v){
//
//        Verticle userVerticle = new UserVerticle();
//        vertx.deployVerticle(userVerticle)
//                .compose( string -> {
//                    System.out.println("[initializeUserVerticle] :"+string);
//                    return Future.future(p->p.complete());
//                });
//
//        return Future.future(p->p.complete());
//    }
	
	@Override
    public void start(Promise<Void> startPromise) {
        // Configure the HTTP server options
        HttpServerOptions options = new HttpServerOptions()
                .setPort(8085)
                .setHost("localhost")
                .setDecompressionSupported(true); // Enable decompression for HTTP requests

        // Create the HTTP server with the specified options
        HttpServer server = vertx.createHttpServer(options);
        Router router = Router.router(vertx);
        WebSocketServer webSocketServer = new WebSocketServer();

        // Set up the WebSocket handler
        server.webSocketHandler(webSocketServer::handleWebSocket);
        server.requestHandler(router);

        // Start the server
        server.listen(result -> {
            if (result.succeeded()) {
                startPromise.complete();
                System.out.println("Server is listening on port 8085");
            } else {
                startPromise.fail(result.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }

}
