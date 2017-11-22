package com.settlerofcatan.websocketconnectionmanager.actor.manager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.settlerofcatan.websocketconnectionmanager.actor.AppUser;
import com.settlerofcatan.websocketconnectionmanager.handler.SessionManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserManager extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final Map<String, ActorRef> userActorsByEmail = new HashMap<>();
    private final Map<ActorRef, String> emailsByUserActor = new HashMap<>();
    private SessionManager sessionManager;

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

    /********************* MESSAGES ************************/

    public static final class UserConnected {
        private String email;

        public UserConnected(String email) {
            this.email = email;
        }
    }

    public static final class UserDisconnected {
        private String email;

        public UserDisconnected(String email) {
            this.email = email;
        }
    }

    public static final class RegisterSessionManager {
        private SessionManager sessionManager;

        public RegisterSessionManager(SessionManager sessionManager) {
            this.sessionManager = sessionManager;
        }
    }

    /********************* MESSAGES ************************/

    private void onUserConnected(UserConnected userConnected) {
        log.info("User connected");
        ActorRef user = userActorsByEmail.get(userConnected.email);

        if (user == null) {
            user = getContext().actorOf(AppUser.props(userConnected.email, "test", getSelf()), "user: " + userConnected.email);
            context().watch(user);
            userActorsByEmail.put(userConnected.email, user);
            emailsByUserActor.put(user, userConnected.email);
        }
    }

    private void onUserDisconnected(UserDisconnected userDisconnected) {
        ActorRef user = userActorsByEmail.get(userDisconnected.email);
        user.tell(new AppUser.Disconnected(), getSelf());
    }

    private void onReigsterSessionManager(RegisterSessionManager registerSessionManager) {

        this.sessionManager = registerSessionManager.sessionManager;
    }
    private void onChatMessageToUser(ChatRoom.ChatMessageToUser chatMessageToUser){

        WebSocketSession session = (WebSocketSession) sessionManager.getActiveSessions().get(chatMessageToUser.getEmail());
        try {
            session.sendMessage(new TextMessage(chatMessageToUser.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onTerminated(Terminated terminated) {
        ActorRef user = terminated.getActor();
        String email = emailsByUserActor.get(user);
        userActorsByEmail.remove(email);
        emailsByUserActor.remove(user);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserConnected.class, this::onUserConnected)
                .match(UserDisconnected.class, this::onUserDisconnected)
                .match(Terminated.class, this::onTerminated)
                .match(RegisterSessionManager.class, this::onReigsterSessionManager)
                .build();

    }
}
