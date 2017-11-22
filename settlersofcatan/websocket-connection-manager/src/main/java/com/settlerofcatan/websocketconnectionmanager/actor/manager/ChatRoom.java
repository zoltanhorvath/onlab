package com.settlerofcatan.websocketconnectionmanager.actor.manager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends AbstractActor {

    private ActorRef owner;
    private String name;
    private Map<String, ActorRef> participants = new HashMap<>();

    public ChatRoom(ActorRef owner, String name) {
        this.owner = owner;
        this.name = name;
    }


    public static Props props(ActorRef owner, String name) {
        return Props.create(ChatRoom.class, owner, name);
    }

    public static final class ChatMessageToRoom {
        String text;
        String from;
        String room;

        public ChatMessageToRoom(String text, String from, String room) {
            this.text = text;
            this.from = from;
            this.room = room;
        }

        public String getText() {
            return text;
        }

        public String getFrom() {
            return from;
        }

        public String getRoom() {
            return room;
        }
    }

    public static final class ChatMessageToUser{
        private String text;
        private String email;

        public ChatMessageToUser(String text, String email) {
            this.text = text;
            this.email = email;
        }

        public String getText() {
            return text;
        }

        public String getEmail() {
            return email;
        }
    }

    private void onChatMessage(ChatMessageToRoom chatMessageToRoom) {
        participants.forEach((email, userRef) -> {
            userRef.forward(new ChatMessageToUser(chatMessageToRoom.text, email), getContext());
        });
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ChatMessageToRoom.class, this::onChatMessage).build();
    }
}
