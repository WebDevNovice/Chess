package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import responseobjects.ErrorMessage;
import service.execeptions.BadRequestException;
import service.serverservices.AuthServices;
import service.serverservices.GameServices;
import service.wsservices.*;
import websocket.commands.UserGameCommand;

@WebSocket
public class WSHandler {
    AuthServices authServices;
    WSSessions wsSessions;
    GameServices gameServices;

    public WSHandler(AuthServices authServices){
        this.authServices = authServices;
        this.wsSessions = new WSSessions();
        this.gameServices = new GameServices(new GameDAOSQL());
    }

    @OnWebSocketConnect
    public void onConnect(Session session, String message) {
        System.out.println("onConnect: " + message);
    }

    @OnWebSocketClose
    public void onClose(Session session) {
        System.out.println("onClose: " + session);
    }

    @OnWebSocketError
    public void onError(Throwable error) {
        System.out.println("onError: " + error);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = authServices.getAuth(command.getAuthString()).getUsername();

            wsSessions.addSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username);
                case LEAVE -> leaveGame(session, username);
                case RESIGN -> resign(session, username);
                default -> throw new IllegalStateException("Unexpected value: " + command.getCommandType());
            }
        } catch (BadRequestException | DataAccessException ex) {
            sendMessage(session, new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }


    private void connect(Session session, String username, UserGameCommand command) {
        //this will connect the player to a game
        //Broadcast a message to everyone that a new player joined and the color they are playing as
        //Broadcast that someone is watching and which team they are rooting for (i.e. watching)
        AuthData authData = new AuthData(username, command.getAuthString());


    }

    private void makeMove(Session session, String username) {
        //this allows a player to make a move
        //broadcast to everyone except the player who made the move what the move was
        //Should this should also return if they are in Check, CheckMate, or Stalemate?

    }

    private void leaveGame(Session session, String usernamed) {
        //Broadcast to everyone that a player left

    }

    private void resign(Session session, String username) {
        //Broadcast that a player resigns
        //print the board, but don't allow anymore moves to be made
    }

    private void sendMessage(Session session, ErrorMessage errorMsg) {}

    private void broadcast(Integer gameID, ErrorMessage errorMsg) {}
}
