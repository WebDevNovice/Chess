package service;

import Models.AuthData;
import Models.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import java.util.Collection;

public class GameServices {
    GameDA0_interface gameDoa;

    public GameServices(GameDA0_interface gameDoa) {
        this.gameDoa = gameDoa;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = gameDoa.listGames();
        if (games.isEmpty()) {
            games = null;
            return games;
        }
        return games;
    }

    public Integer CreateGame(String gameName) throws DataAccessException {
        for (GameData game : gameDoa.getGameDatabase().values()) {
            if (game.getGameName().equals(gameName)) {
                throw new DataAccessException("Error: Game name already exists");
            }
        }
        return gameDoa.createGame(gameName);
    }

    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException {
        if (gameDoa.getGameDatabase().containsKey(gameId)){
            GameData game = gameDoa.joinGame(playerColor, gameId, authData);
            return game;
        }else{
            throw new BadRequestException("Error: Game does not exist");
        }
    }


}

