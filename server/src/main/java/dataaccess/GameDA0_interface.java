package dataaccess;

import Models.AuthData;
import Models.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public interface GameDA0_interface {
    Integer createGame(String gameName) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException;
    Objects updateGame(GameData game) throws DataAccessException;
    void clearGamedatabase();
    HashMap<Integer, GameData> getGameDatabase() throws DataAccessException;
}
