package com.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.time.LocalTime;
import java.util.UUID;

public class UserVerticle extends AbstractVerticle {

    String uuid = null;
    EventBus eventBus = null;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        setUserId()
            .compose(this::setEventBus)
            .compose(this::getMsg)
            .compose(this::sendMsg)
            .onComplete( handler -> {
                if(handler.succeeded()){
                    System.out.println("User Verticle Created Successfully...");
                    startPromise.complete();
                }else{
                    System.out.println("User Verticle Creation Failed...");
                    startPromise.fail(handler.cause());
                }
            });
    }

    public Future<EventBus> setEventBus(Void v){
        eventBus = this.vertx.eventBus();
        System.out.println("EventBus Initialized... ");

        return Future.future(p -> p.complete(eventBus));
    }

    public Future<Void> setUserId(){
        uuid = UUID.randomUUID().toString();

        System.out.println("UserId Assigned: "+uuid);

        return Future.future(p -> p.complete());
    }

    public Future<EventBus> getMsg(EventBus eb){
        eb.consumer("SERVER_PROPAGATION_TOPIC", handle -> {
            System.out.println( "@"+uuid+">> Received Msg : "+ handle.body().toString());
        });

        return Future.future( p->p.complete(eb));
    }

    public Future<Void> sendMsg(EventBus eb){

        String msg = "New Msg ".concat(LocalTime.now().toString());
        JsonObject jo = new JsonObject();
        jo.put("Sender", uuid);
        jo.put("MSG", msg);

        eb.send("SERVER", jo.toString());

        return Future.future( p->p.complete());
    }


}
