package service;

import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;

import java.util.Collection;

public class GameServices {
    GameDA0Interface gameDoa;

    public GameServices(GameDA0Interface gameDoa) {
        this.gameDoa = gameDoa;
    }

    public Collection<GameData> listGames() throws DataAccessException, ResponseException {
        Collection<GameData> games = gameDoa.listGames();
        if (games.isEmpty()) {
            games = null;
            return games;
        }
        return games;
    }

    public Integer createGame(String gameName) throws DataAccessException, ResponseException {
        for (GameData game : gameDoa.getGameDatabase().values()) {
            if (game.getGameName().equals(gameName)) {
                throw new DataAccessException("Error: Game name already exists");
            }
        }
        return gameDoa.createGame(gameName);
    }

    public GameData joinGame(String playerColor, Integer gameId, AuthData authData)
            throws DataAccessException, BadRequestException, UnvailableTeamException {

        if (gameDoa.getGameDatabase().containsKey(gameId)){
            GameData game = gameDoa.joinGame(playerColor, gameId, authData);
            return game;
        }else{
            throw new BadRequestException("Error: Game does not exist");
        }
    }


}

