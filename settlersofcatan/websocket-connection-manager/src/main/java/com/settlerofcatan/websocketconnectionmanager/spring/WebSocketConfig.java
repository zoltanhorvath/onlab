package com.settlerofcatan.websocketconnectionmanager.spring;

import akka.actor.ActorSystem;
import com.settlerofcatan.websocketconnectionmanager.actor.manager.ChatRoomManager;
import com.settlerofcatan.websocketconnectionmanager.actor.manager.UserManager;
import com.settlerofcatan.websocketconnectionmanager.handler.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.settlerofcatan.websocketconnectionmanager.spring.SpringExtension.SPRING_EXTENSION_PROVIDER;
import static com.settlerofcatan.websocketconnectionmanager.Constants.*;

@Configuration
@EnableWebSocket
@ComponentScan
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(myHandler(), "/connect/*");
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new SessionManager(actorSystem());
    }

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create(ROOT_ACTOR_NAME);
        system.actorOf(UserManager.props(),USER_MANAGER_ACTOR_NAME);
        system.actorOf(ChatRoomManager.props(), CHAT_ROOM_MANAGER_NAME);
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }
}
