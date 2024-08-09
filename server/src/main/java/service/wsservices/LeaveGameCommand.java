package service.wsservices;

import dataaccess.sqlMemory.GameDAOSQL;
import service.serverservices.GameServices;
import websocket.commands.UserGameCommand;

public class LeaveGameCommand {
    Integer gameID;
    String playerColor;
    GameServices gameServices;
    GameDAOSQL gameDAOSQL;

    public LeaveGameCommand(Integer gameID, String playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
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

    }
}
