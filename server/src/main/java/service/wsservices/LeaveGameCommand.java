package service.wsservices;

import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import service.serverservices.GameServices;
import websocket.commands.UserGameCommand;

public class LeaveGameCommand {
    Integer gameID;
    String playerColor;
    GameServices gameServices;
    GameDAOSQL gameDAOSQL;
    String username;

    public LeaveGameCommand(Integer gameID, String playerColor, String username) {
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.username = username;
        gameDAOSQL = new GameDAOSQL();
        gameServices = new GameServices(gameDAOSQL);
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void leaveGame() {
        try {
            gameDAOSQL.updatePlayer(gameID, playerColor, username);
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
