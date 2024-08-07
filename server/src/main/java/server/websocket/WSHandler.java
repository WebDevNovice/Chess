package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import responseobjects.ErrorMessage;
import service.execeptions.BadRequestException;
import service.serverservices.AuthServices;
import service.websocketservices.*;
import websocket.commands.UserGameCommand;

@WebSocket
public class WSHandler {
    AuthServices authServices;

    public WSHandler(AuthServices authServices){
        this.authServices = authServices;
    }

    @OnWebSocketConnect
    public void onConnect(Session session, String message) {
    }

    @OnWebSocketClose
    public void onClose(Session session) {}

    @OnWebSocketError
    public void onError(Throwable error) {}

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = authServices.getAuth(command.getAuthString()).getUsername();

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) new ConnectCommand(command.getCommandType()));
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) new MakeMoveCommand(command.getCommandType()));
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) new LeaveGameCommand(command.getCommandType()));
                case RESIGN -> resign(session, username, (ResignCommand) new ResignCommand(command.getCommandType()));
                default -> throw new IllegalStateException("Unexpected value: " + command.getCommandType());
            }
        } catch (BadRequestException | DataAccessException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void saveSession(int gameID, Session session) {
    }

    private void connect(Session session, String username, ConnectCommand command) {}

    private void makeMove(Session session, String username, MakeMoveCommand command) {}

    private void leaveGame(Session session, String username, LeaveGameCommand command) {}

    private void resign(Session session, String username, ResignCommand command) {}

    private void sendMessage(RemoteEndpoint session, ErrorMessage errorMsg) {}

    private void broadcast(Session session, ErrorMessage errorMsg) {}
}
