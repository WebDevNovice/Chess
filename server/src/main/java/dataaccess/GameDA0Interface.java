package dataaccess;

import dataaccess.sqlMemory.ResponseException;
import model.AuthData;
import model.GameData;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;

import java.util.Collection;
import java.util.HashMap;

public interface GameDA0Interface {
    Integer createGame(String gameName) throws DataAccessException, ResponseException;
    Collection<GameData> listGames() throws DataAccessException, ResponseException;
    GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException, ResponseException;
    void clearGamedatabase() throws ResponseException, DataAccessException;
    HashMap<Integer, GameData> getGameDatabase() throws DataAccessException, ResponseException;
}
