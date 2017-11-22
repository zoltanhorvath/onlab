package com.settlerofcatan.websocketconnectionmanager.handler;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.util.Timeout;
import com.settlerofcatan.websocketconnectionmanager.actor.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static com.settlerofcatan.websocketconnectionmanager.Constants.USER_MANAGER_ACTOR_NAME;

@Component
public class SessionManager extends TextWebSocketHandler {

    private ConcurrentHashMap<String , WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    private ActorSystem system;
    private ActorRef userManager;

    @Autowired
    public SessionManager(ActorSystem system) {
        this.system = system;

        ActorSelection actorSelection =  this.system.actorSelection("user/"+USER_MANAGER_ACTOR_NAME);

        Future<ActorRef> actorRefFuture = actorSelection.resolveOne(new FiniteDuration(3,TimeUnit.SECONDS));

        actorRefFuture.onComplete(new OnComplete<ActorRef>() {
            @Override
            public void onComplete(Throwable failure, ActorRef actor) throws Throwable {
                if(failure == null){
                    userManager = actor;

                }else{
                    throw failure;
                }
            }
        }, this.system.dispatcher());

        FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
        Timeout timeout = Timeout.durationToTimeout(duration);
        ask(userManager, new UserManager.RegisterSessionManager(this),timeout);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        activeSessions.put(session.getId(), session);
        FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
        Timeout timeout = Timeout.durationToTimeout(duration);
        ask(userManager,new UserManager.UserConnected(session.getUri().toString()),timeout);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        activeSessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    public ConcurrentHashMap getActiveSessions() {
        return activeSessions;
    }
}
