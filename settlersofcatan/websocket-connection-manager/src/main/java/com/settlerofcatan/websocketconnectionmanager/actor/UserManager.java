package com.settlerofcatan.websocketconnectionmanager.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserManager extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final Map<String, ActorRef> userActorsByEmail = new HashMap<>();

    public static Props props() {
        return Props.create(UserManager.class);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

    private void onUserConnected(UserConnected userConnected){
        log.info("User connected");
    }

    public static final class UserConnected{
        private String email;

        public UserConnected(String email) {
            this.email = email;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserConnected.class,this::onUserConnected).build();
    }
}
