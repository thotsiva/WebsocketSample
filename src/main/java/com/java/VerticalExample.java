package com.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class VerticalExample extends AbstractVerticle {
	
	@Override
	public void start(Promise<Void> startPromise) {
        vertx.createHttpServer()
        .requestHandler(r -> r.response().end("Vertex demo sample websocket")
        )
        .listen(config().getInteger("http.port", 9090), 
          result -> {
            if (result.succeeded()) {
            	startPromise.complete();
            } else {
            	startPromise.fail(result.cause());
            }
        });
    }

	
	@Override
	public void stop() {
		System.out.println("Shutting down application");
	}
	
	public static void main(String[] args) {
	    Vertx vertx = Vertx.vertx();
	    vertx.deployVerticle(new VerticalExample());
	}
}
