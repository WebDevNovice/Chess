package wsfacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
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

    UserGameCommand command;
    GameHandler gameHandler;
    HashMap<Integer, Set<Session>> sessions;

    public WSFacade(String url, UserGameCommand command) {
        try {
            this.command = command;
            this.gameHandler = new GameUI(this);

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage.ServerMessageType messageType = new Gson().fromJson(message,
                            ServerMessage.ServerMessageType.class);
                    switch (messageType) {
                        case LOAD_GAME:
                            break;
                        case NOTIFICATION:
                            break;
                        case ERROR:
                            break;
                        default:
                            break;
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

    private void connect(Session session, UserGameCommand command) throws IOException {
        String message = new Gson().toJson(command);
        session.getBasicRemote().sendText(message);
    }

    private void makeMove(Session session, UserGameCommand command) {}

    private void leaveGame(Session session, UserGameCommand command) {}

    private void resignGame(Session session, UserGameCommand command) {}

    public void sendMessage(Session session, UserGameCommand command) throws IOException {
        String message = new Gson().toJson(command);
        session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return session;
    }

}