package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
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
import java.util.HashMap;
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

//    @OnWebSocketConnect
//    public void onConnect(Session session, String message) {
//        System.out.println("onConnect: " + message);
//    }
//
//    @OnWebSocketError
//    public void onError(Throwable error) {
//        System.out.println("onError: " + error);
//    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = authServices.getAuth(command.getAuthString()).getUsername();

            wsSessions.addSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
                default -> throw new IllegalStateException("Error: " + command.getCommandType());
            }
        } catch (Exception ex) {
            sendMessage(session, new WSErrorMsg(errorMsg, ex.getMessage()));
        }
    }


    private void connect(Session session, String username, UserGameCommand command) throws ResponseException,
                                                    UnvailableTeamException, BadRequestException, DataAccessException {

        AuthData authData = new AuthData(username, command.getAuthString());
        ConnectCommand connectCommand = new ConnectCommand(authData, command.getGameID());
        GameData gameData = connectCommand.connect();

        if (connectCommand.getTeamColor() == null){
            String message = String.format("HAIL! %s is a cosmic observer, prove yourself worthy",
                                                    authData.getUsername());
            ServerMessage serverMessage = new WSNotificationMsg(notificationMsg, message);
            broadcast(gameData.gameID, serverMessage, session);

            serverMessage = new WSLoadGameMsg(loadGameMsg, gameData);
            sendMessage(session, serverMessage);
        }
        else {
            String message = String.format("On Guard! %s is commanding the %s forces, HooZAH!",
                    authData.getUsername(), connectCommand.getTeamColor());
            ServerMessage serverMessage = new WSNotificationMsg(notificationMsg, message);
            broadcast(gameData.gameID, serverMessage, session);

            serverMessage = new WSLoadGameMsg(loadGameMsg, gameData);
            sendMessage(session, serverMessage);
        }

    }

    private void makeMove(Session session, String username, UserGameCommand command) {
        //this allows a player to make a move
        //broadcast to everyone except the player who made the move what the move was
        //Should this should also return if they are in Check, CheckMate, or Stalemate?

    }

    private void leaveGame(Session session, String username, UserGameCommand command) throws BadRequestException,
                                                                            ResponseException, DataAccessException {
        //Broadcast to everyone that a player left
        LeaveGameCommand leaveGameCommand = new LeaveGameCommand(command.getGameID(), username);
        if (leaveGameCommand.getPlayerColor() == null){
            String message = String.format("Our cosmic observer known as %s is gone... Continue as you were",
                                                                                                    username);
            ServerMessage serverMessage = new WSNotificationMsg(notificationMsg, message);
            broadcast(command.getGameID(), serverMessage, session);

            wsSessions.removeSession(leaveGameCommand.getGameID(), session);
            return;
        }
        leaveGameCommand.leaveGame();
        if (leaveGameCommand.isLeftGame()){

            String message = String.format("Player %s left the game! Cowardice!!!", username);
            ServerMessage serverMessage = new WSNotificationMsg(notificationMsg, message);
            broadcast(command.getGameID(), serverMessage, session);


            wsSessions.removeSession(command.getGameID(), session);
        }
        else {
            String message = String.format("Error: %s did not leave the game", username);
            throw new BadRequestException(message);
        }
    }

    private void resign(Session session, String username, UserGameCommand command) throws BadRequestException, DataAccessException {
        //Broadcast that a player resigns.
        //Print the board, but don't allow anymore moves to be made.
        //I think I'll have the client hold a local copy of the game and have it update as we play and push moves to the
        //database. So here I'll just send a blank game, so they cannot reference it and draw the last board. I don't
        //like this.
        String message = String.format("Player %s has resigned the game!", username);
        ServerMessage serverMessage = new WSNotificationMsg(notificationMsg, message);
        broadcast(command.getGameID(), serverMessage, null);

        ResignCommand resignCommand = new ResignCommand(command.getGameID());
        GameData gameData = resignCommand.resignGame();

        HashMap<Integer, Set<Session>> connections = wsSessions.getConnections();
        connections.remove(command.getGameID());
    }

    private void sendMessage(Session session, ServerMessage message) {
        String jsonMsg= new Gson().toJson(message);
        if (session.isOpen())
            try{
                session.getRemote().sendString(jsonMsg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private void broadcast(Integer gameID, ServerMessage message, Session excludeSession) {
        try{
            Set<Session> sessions = wsSessions.getSession(gameID);
            for (Session session : sessions) {
                if (session.isOpen() && !session.equals(excludeSession)) {
                    String jsonMsg = new Gson().toJson(message); //how will the client know of all the child classes it is?
                    session.getRemote().sendString(jsonMsg);
                }
            }
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
