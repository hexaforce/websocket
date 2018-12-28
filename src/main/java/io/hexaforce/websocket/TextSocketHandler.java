package io.hexaforce.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TextSocketHandler extends AbstractTextWebSocketHandler<> {

    public TextSocketHandler(ObjectMapper objectMapper, DataChanneGenerator<> dataChanneGenerator) {
        super(objectMapper, dataChanneGenerator);
    }

//    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String value = objectMapper.writeValueAsString(message);
//        //Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
//        /*
//         * for(WebSocketSession webSocketSession : sessions) {
//         * webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") +
//         * " !")); }
//         */
//        session.sendMessage(new TextMessage("Hello " + value + " !"));
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // the messages will be broadcasted to all users.
//        sessions.add(session);
//    }

}