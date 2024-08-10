package service.wsservices;

import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;
import service.serverservices.GameServices;

import java.util.Collection;

public class ConnectCommand {
    String playerColor;
    AuthData authData;
    GameServices gameServices;
    Integer gameID;
    GameDAOSQL gameDAOSQL;


    public ConnectCommand(AuthData authData, Integer gameID) {
        this.authData = authData;
        this.gameID = gameID;
        gameDAOSQL = new GameDAOSQL();
        this.gameServices = new GameServices(gameDAOSQL);

        PlayerColorHelper playerColorHelper = new PlayerColorHelper();
        try {
            this.playerColor = playerColorHelper.setPlayerColor(gameServices, gameID, authData.getUsername());
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameData connect() throws ResponseException, UnvailableTeamException, BadRequestException, DataAccessException {
        GameData gameData;
            Collection<GameData> games = gameServices.listGames();
            for (GameData game: games) {
                if (game.getGameID() == gameID){
                    gameData = game;
                    return gameData;
                }
            }
            throw new DataAccessException("Error: Could not connect to game");
    }

    public String getTeamColor() {
        return playerColor;
    }
}
