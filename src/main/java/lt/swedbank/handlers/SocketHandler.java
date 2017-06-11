package lt.swedbank.handlers;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {

        //for(WebSocketSession webSocketSession : sessions) {
        //    Map value = new Gson().fromJson(message.getPayload(), Map.class);
        //    webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
        //}
        session.sendMessage(new TextMessage("Greetings from back-end"));
        System.out.println("Received a message: " + message.getPayload());
        System.out.println("In return message for a current session has been sent");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
        System.out.println("Connection has been established");
        session.sendMessage(new TextMessage("Back-end says welcome"));
    }
}
