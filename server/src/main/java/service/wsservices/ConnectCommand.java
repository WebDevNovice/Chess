package service.wsservices;

import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;
import service.serverservices.GameServices;
import websocket.commands.UserGameCommand;

public class ConnectCommand {
    AuthData authData;
    GameServices gameServices;
    Integer gameID;
    String teamColor;
    GameDAOSQL gameDAOSQL;

    ConnectCommand(AuthData authData, Integer gameID, String teamColor) {
        this.authData = authData;
        this.gameID = gameID;
        this.teamColor = teamColor;
        gameDAOSQL = new GameDAOSQL();
        this.gameServices = new GameServices(gameDAOSQL);
    }

    public GameData connect() throws ResponseException, UnvailableTeamException, BadRequestException, DataAccessException {
        GameData gameData = gameServices.joinGame(teamColor, gameID, authData);
        return gameData;
    }

}
