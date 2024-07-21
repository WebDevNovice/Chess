package dataaccess.RamMemory;

import Models.AuthData;
import Models.GameData;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import java.util.*;

public class GameDAO_RAM implements GameDA0_interface {
    HashMap<Integer, GameData> gameDataHashMap;
    Integer gameID = 1;

    public GameDAO_RAM() {
        gameDataHashMap = new HashMap<>();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        for (GameData game : gameDataHashMap.values()) {
            if (game.getGameName().equals(gameName)) {
                throw new DataAccessException("Game name already exists");
            }
            continue;
        }
        GameData newGame = new GameData("","",gameName, new ChessGame(), gameID);
        gameDataHashMap.put(gameID++, newGame);

        return newGame.getGameID();
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return gameDataHashMap.values();
    }

    @Override
    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException {
        var username = authData.getUsername();
        if (gameDataHashMap.containsKey(gameId)){
            if (playerColor.equals("WHITE")) {
                if (gameDataHashMap.get(gameId).getWhiteUsername().isEmpty()) {
                    gameDataHashMap.get(gameId).setWhiteUsername(authData.getUsername());
                    return gameDataHashMap.get(gameId);
                }
                throw new DataAccessException("OOPS! Someone has already taken that team Color");
            }else{
                if (gameDataHashMap.get(gameId).getBlackUsername().isEmpty()) {
                    gameDataHashMap.get(gameId).setBlackUsername(authData.getUsername());
                    return gameDataHashMap.get(gameId);
                }
                throw new DataAccessException("OOPS! Someone has already taken that team Color");
            }
        }else{
            throw new DataAccessException("Game does not exist");
        }
    }

    @Override
    public Objects updateGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public void clearGamedatabase() {
            gameDataHashMap.clear();
    }

    public HashMap<Integer, GameData> getGameDatabase() throws DataAccessException{
        return gameDataHashMap;
    }
}
