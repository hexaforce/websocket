package io.hexaforce.websocket.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import io.hexaforce.websocket.model.SessionGroup;

/**
 * @author tantakatomokazu
 *
 * @param <G> SessionGroup
 * @param <C> SocketChanne
 * @param <T> Data
 */
public abstract class AbstractTextWebSocketHandler<G, C, T> extends TextWebSocketHandler {

    protected ObjectMapper objectMapper;

    protected Map<G, Set<WebSocketSession>> sessionPool;

    public AbstractTextWebSocketHandler(ObjectMapper objectMapper, DataChanneGenerator<G, C> dataChanneGenerator) {
        this.objectMapper = objectMapper;
        this.sessionPool = new ConcurrentHashMap<>();
        this.sessionPool.put(dataChanneGenerator.getSessionGroup(), Sets.newConcurrentHashSet());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SessionGroup sessionGroup = getSessionGroup(session);
        if (sessionPool.containsKey(sessionGroup)) {
            sessionPool.get(sessionGroup).add(session);
            afterSessionRegist(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionGroup sessionGroup = getSessionGroup(session);
        if (sessionPool.containsKey(sessionGroup)) {
            sessionPool.get(sessionGroup).remove(session);
            afterSessionRelease(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // TODO Login Check here!!

        userRequest(session);

    }

    public static interface DataChanneGenerator<G, C> {
        
        G getSessionGroup();

        Set<C> channeList();
        
    }

    public abstract SessionGroup getSessionGroup(WebSocketSession session);

    public abstract void afterSessionRegist(WebSocketSession session);

    public abstract void afterSessionRelease(WebSocketSession session);

    public abstract void userRequest(WebSocketSession session);

    public abstract void broadcast(T data);
    
    public abstract void multicast(T data);
    
    public abstract void unicast(T data);

}
