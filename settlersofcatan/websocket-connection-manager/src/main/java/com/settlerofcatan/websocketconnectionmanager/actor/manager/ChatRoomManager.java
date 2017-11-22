package com.settlerofcatan.websocketconnectionmanager.actor.manager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomManager extends AbstractActor {

    private final LoggingAdapter loggingAdapter = Logging.getLogger(getContext().getSystem(), this);

    private Map<String,ActorRef> chatRoomsByName = new HashMap<>();
    private Map<ActorRef,String> chatRoomsByRef = new HashMap<>();

    public static Props props() {
        return Props.create(ChatRoomManager.class);
    }



    @Override
    public Receive createReceive() {
        return null;
    }
}
