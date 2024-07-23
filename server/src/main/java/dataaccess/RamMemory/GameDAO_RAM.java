package dataaccess.RamMemory;

import Models.AuthData;
import Models.GameData;
import Services.BadRequestException;
import Services.UnvailableTeamException;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import javax.servlet.UnavailableException;
import java.util.*;

public class GameDAO_RAM implements GameDA0_interface {
    HashMap<Integer, GameData> gameDataHashMap;
    Integer gameID = 1;

    public GameDAO_RAM() {
        gameDataHashMap = new HashMap<>();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        GameData newGame = new GameData("","",gameName, new ChessGame(), gameID);
        gameDataHashMap.put(gameID++, newGame);
        return newGame.getGameID();
    }

    @Override
    public Collection<ChessGame> listGames() throws DataAccessException {
        Collection<GameData> gameData = gameDataHashMap.values();
        Collection<ChessGame> chessGames = new ArrayList<>();
        for (GameData gameData1 : gameData) {
            chessGames.add(gameData1.getGame());
        }
        return chessGames;
    }

    @Override
    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws UnvailableTeamException, BadRequestException {
        var username = authData.getUsername();
        if (gameDataHashMap.get(gameId) == null) {
            if (playerColor.equals("WHITE")) {
                if (gameDataHashMap.get(gameId).getWhiteUsername().isEmpty()) {
                    gameDataHashMap.get(gameId).setWhiteUsername(username);
                    return gameDataHashMap.get(gameId);
                }
                throw new UnvailableTeamException("Error: OOPS! Someone has already taken that team Color");
            }else{
                if (gameDataHashMap.get(gameId).getBlackUsername().isEmpty()) {
                    gameDataHashMap.get(gameId).setBlackUsername(username);
                    return gameDataHashMap.get(gameId);
                }
                throw new UnvailableTeamException("Error: OOPS! Someone has already taken that team Color");
            }
        }
        throw new BadRequestException("Error: Must Declare Team Color");
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
