package wsfacade;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.WSErrorMsg;
import websocket.messages.WSLoadGameMsg;
import websocket.messages.WSNotificationMsg;
import wsfacade.gamehandlers.GameHandler;
import wsfacade.gamehandlers.GameUI;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

public class WSFacade extends Endpoint { ;
    Session session;
    String username;

    UserGameCommand command;
    GameHandler gameHandler;
    HashMap<Integer, Set<Session>> sessions;

    public WSFacade(String url, UserGameCommand command, String username) {
        try {
            this.command = command;
            this.gameHandler = new GameUI(this);
            this.username = username;

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message,
                            ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME:
                            //re-deserialize the message into the correct class
                            WSLoadGameMsg loadGameMsg = new Gson() .fromJson(message, WSLoadGameMsg.class);
                            gameHandler.updateGame(loadGameMsg.getGameData());
                        case NOTIFICATION:
                            WSNotificationMsg notificationMsg = new Gson().fromJson(message, WSNotificationMsg.class);
                            gameHandler.printMessage(notificationMsg.getMessage());
                        case ERROR:
                            WSErrorMsg errorMsg = new Gson().fromJson(message, WSErrorMsg.class);
                            gameHandler.printMessage(errorMsg.getErrorMessage());
                        default:
                            throw new RuntimeException("Error: Unknown ServerMessageType: " +
                                                        serverMessage.getServerMessageType());
                    }
                }
            });
        } catch (DeploymentException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public UserGameCommand.CommandType getCommandType() {
        return command.getCommandType();
    }

    public void connect(Session session, UserGameCommand command) throws IOException {
        sendMessage(session, command);
    }

    private void makeMove(Session session, UserGameCommand command) throws IOException {
        sendMessage(session, command);
    }

    private void leaveGame(Session session, UserGameCommand command) throws IOException {
        sendMessage(session, command);
    }

    private void resignGame(Session session, UserGameCommand command) throws IOException {
        sendMessage(session, command);
    }

    private void sendMessage(Session session, UserGameCommand command) throws IOException {
        String message = new Gson().toJson(command);
        session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return session;
    }

}