package com.settlerofcatan.websocketconnectionmanager.spring;

import akka.actor.ActorSystem;
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
        ActorSystem system = ActorSystem.create("akka-spring-demo");
        SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
        return system;
    }
}
