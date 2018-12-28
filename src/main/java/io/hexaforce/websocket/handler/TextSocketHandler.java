package io.hexaforce.websocket.handler;

//import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.hexaforce.websocket.config.AbstractTextWebSocketHandler;
import io.hexaforce.websocket.config.ChartChanneGenerator;
import io.hexaforce.websocket.model.BoardEntity;
import io.hexaforce.websocket.model.DataChannel1;
import io.hexaforce.websocket.model.SessionGroup;

@Component
public class TextSocketHandler extends AbstractTextWebSocketHandler<SessionGroup, DataChannel1, BoardEntity> {

    public TextSocketHandler(ObjectMapper objectMapper, ChartChanneGenerator dataChanneGenerator) {
        super(objectMapper, dataChanneGenerator);
    }

    @Override
    public SessionGroup getSessionGroup(WebSocketSession session) {
        return SessionGroup.BUY;
    }

    @Override
    public void afterSessionRegist(WebSocketSession session) {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterSessionRelease(WebSocketSession session) {
        // TODO Auto-generated method stub
    }

    @Override
    public void userRequest(WebSocketSession session) {
        // TODO Auto-generated method stub

    }

    @Override
    public void broadcast(BoardEntity data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unicast(BoardEntity data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void multicast(BoardEntity data) {
        // TODO Auto-generated method stub
        
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