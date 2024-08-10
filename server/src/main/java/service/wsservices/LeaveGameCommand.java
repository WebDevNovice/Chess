package service.wsservices;

import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import service.serverservices.GameServices;
import websocket.commands.UserGameCommand;

import java.util.Collection;

public class LeaveGameCommand {
    Integer gameID;
    String playerColor;
    GameServices gameServices;
    GameDAOSQL gameDAOSQL;
    String username;

    public LeaveGameCommand(Integer gameID, String username) {
        this.gameID = gameID;
        this.username = username;
        gameDAOSQL = new GameDAOSQL();
        gameServices = new GameServices(gameDAOSQL);

        PlayerColorHelper playerColorHelper = new PlayerColorHelper();
        try {
            this.playerColor = playerColorHelper.setPlayerColor(gameServices, gameID, username);
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void leaveGame() {
        try {
            if (playerColor == null) {
                return;
            }
            else {
                gameDAOSQL.updatePlayer(gameID, playerColor, null);
            }
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isLeftGame() throws ResponseException, DataAccessException {
        Collection<GameData> games = gameDAOSQL.listGames();
        for (GameData gameData : games) {
            if (gameData.getGameID() == gameID){
                if (playerColor == "WHITE" && gameData.getWhiteUsername() == null) {
                    return true;
                }
                else if (playerColor == "BLACK" && gameData.getBlackUsername() == null) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }
}
