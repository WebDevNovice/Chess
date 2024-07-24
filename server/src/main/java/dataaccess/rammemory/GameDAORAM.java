package dataaccess.rammemory;

import model.AuthData;
import model.GameData;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;

import java.util.*;

public class GameDAORAM implements GameDA0Interface {
    HashMap<Integer, GameData> gameDataHashMap;
    Integer gameID = 1;

    public GameDAORAM() {
        gameDataHashMap = new HashMap<>();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        GameData newGame = new GameData(null,null,gameName, new ChessGame(), gameID);
        gameDataHashMap.put(gameID++, newGame);
        return newGame.getGameID();
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameData = gameDataHashMap.values();
        return gameData;
    }

    @Override
    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws UnvailableTeamException, BadRequestException {
        var username = authData.getUsername();
        if (playerColor != null) {
            if (playerColor.equals("WHITE")) {
                if (gameDataHashMap.get(gameId).getWhiteUsername()==null) {
                    gameDataHashMap.get(gameId).setWhiteUsername(username);
                    GameData gameData = gameDataHashMap.get(gameId);
                    return gameData;
                }
                throw new UnvailableTeamException("Error: OOPS! Someone has already taken that team Color");
            }else{
                if (gameDataHashMap.get(gameId).getBlackUsername()==null) {
                    gameDataHashMap.get(gameId).setBlackUsername(username);
                    return gameDataHashMap.get(gameId);
                }
                throw new UnvailableTeamException("Error: OOPS! Someone has already taken that team Color");
            }
        }
        throw new BadRequestException("Error: Must Declare Team Color");
    }

    @Override
    public void clearGamedatabase() {
            if (gameDataHashMap != null) {
                gameDataHashMap.clear();
            }
    }

    public HashMap<Integer, GameData> getGameDatabase() throws DataAccessException{
        return gameDataHashMap;
    }
}
