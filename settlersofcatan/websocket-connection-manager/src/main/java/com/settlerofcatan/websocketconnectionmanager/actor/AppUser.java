package com.settlerofcatan.websocketconnectionmanager.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.settlerofcatan.websocketconnectionmanager.actor.manager.ChatRoom;

import java.util.HashMap;
import java.util.Map;

public class AppUser extends AbstractActor {

    private String email;
    private String name;
    private Map<String, ActorRef> chatRooms = new HashMap<>();
    private ActorRef userManager;

    public AppUser(String email, String name, ActorRef userManager) {
        this.email = email;
        this.name = name;
        this.userManager = userManager;
    }

    public static Props props(String email, String name, ActorRef userManager) {
        return Props.create(AppUser.class, email, name, userManager);
    }

    public static final class Disconnected {
    }

    private void onDisconnected(Disconnected disconnected) {
        context().stop(self());
    }

    private void onChatMessageToRoom(ChatRoom.ChatMessageToRoom chatMessageToRoom){
        ActorRef room = chatRooms.get(chatMessageToRoom.getRoom());
        room.forward(chatMessageToRoom,getContext());
    }

    private void onChatMessageToUser(ChatRoom.ChatMessageToUser chatMessageToUser){
        userManager.forward(chatMessageToUser,getContext());
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Disconnected.class, this::onDisconnected)
                .match(ChatRoom.ChatMessageToRoom.class,this::onChatMessageToRoom)
                .match(ChatRoom.ChatMessageToUser.class,this::onChatMessageToUser)
                .build();
    }


}
