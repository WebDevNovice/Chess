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
    AuthData authData;
    GameServices gameServices;
    Integer gameID;
    String teamColor;
    GameDAOSQL gameDAOSQL;

    public ConnectCommand(AuthData authData, Integer gameID, String teamColor) {
        this.authData = authData;
        this.gameID = gameID;
        this.teamColor = teamColor;
        gameDAOSQL = new GameDAOSQL();
        this.gameServices = new GameServices(gameDAOSQL);
    }

    public GameData ConnectPlayer() throws ResponseException, UnvailableTeamException, BadRequestException, DataAccessException {
        GameData gameData = gameServices.joinGame(teamColor, gameID, authData);
        return gameData;
    }

    public Collection<GameData> ConnectObserver() throws ResponseException, UnvailableTeamException, BadRequestException, DataAccessException {
        return gameServices.listGames();
    }

}
