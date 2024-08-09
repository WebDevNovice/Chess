package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import responseobjects.ErrorMessage;
import server.Server;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;
import service.serverservices.AuthServices;
import service.serverservices.GameServices;
import service.wsservices.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.WSErrorMsg;
import websocket.messages.WSLoadGameMsg;
import websocket.messages.WSNotificationMsg;

import java.io.IOException;
import java.util.Set;

@WebSocket
public class WSHandler {
    AuthServices authServices;
    WSSessions wsSessions;
    GameServices gameServices;
    ServerMessage.ServerMessageType notificationMsg = ServerMessage.ServerMessageType.NOTIFICATION;
    ServerMessage.ServerMessageType errorMsg = ServerMessage.ServerMessageType.ERROR;
    ServerMessage.ServerMessageType loadGameMsg = ServerMessage.ServerMessageType.LOAD_GAME;

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
            sendMessage(session, new WSErrorMsg(errorMsg, ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session, new WSErrorMsg(errorMsg, ex.getMessage()));
        }
    }


    private void connect(Session session, String username, UserGameCommand command) {
        //this will connect the player to a game
        //Broadcast a message to everyone that a new player joined and the color they are playing as
        //Broadcast that someone is watching and which team they are rooting for (i.e. watching)
        AuthData authData = new AuthData(username, command.getAuthString());
        ConnectCommand connectCommand = new ConnectCommand(authData, command.getGameID(), command.getTeamColor());
        try{
            GameData gameData = connectCommand.connect();
            String message = String.format("Player + %s connected to the game under the %s banner! HooZAH!!",
                    authData.getUsername(), command.getTeamColor());

            ServerMessage serverMessage = new WSNotificationMsg(notificationMsg, message);
            broadcast(command.getGameID(), serverMessage, session);

            serverMessage = new WSLoadGameMsg(loadGameMsg, gameData);
            broadcast(command.getGameID(), serverMessage, null);


        } catch (ResponseException e) {
            throw new RuntimeException(e);
        } catch (UnvailableTeamException e) {
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }


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

    private void sendMessage(Session session, ServerMessage message) {}

    private void broadcast(Integer gameID, ServerMessage message, Session excludeSession) {
        try{
            Set<Session> sessions = wsSessions.getSession(gameID);
            for (Session session : sessions) {
                if (session.isOpen() && !session.equals(excludeSession)) {
                    session.getRemote().sendString(message.getMessage());
                }
            }
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
