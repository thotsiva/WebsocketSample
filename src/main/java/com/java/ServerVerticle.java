package com.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

public class ServerVerticle extends AbstractVerticle {

    EventBus eb = null;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        eb = this.vertx.eventBus();
        receiveMsgHandler()
            .onComplete( handler -> {
                if(handler.succeeded()){
                    System.out.println("Server Verticle Created Successfully...");
                    startPromise.complete();
                }else{
                    System.out.println("Server Verticle Creation Failed...");
                    startPromise.fail(handler.cause());
                }
            });
    }

    public Future<Void> receiveMsgHandler(){

        MessageConsumer msgConsumer = eb.consumer("SERVER", handler -> {
            sendMsg(handler);
        });

        System.out.println("[Server] Msg Consumer Initialized");

        return Future.future(p->p.complete());
    }

    public void sendMsg(Message msgHandler){
        System.out.println("[Server] Received Msg: "+ msgHandler.body().toString());
        String msg = msgHandler.body().toString();
        eb.publish("SERVER_PROPAGATION_TOPIC", msg);
    }

}
