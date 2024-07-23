package dataaccess;

import Models.AuthData;
import Models.GameData;
import services.BadRequestException;
import services.UnvailableTeamException;

import java.util.Collection;
import java.util.HashMap;

public interface GameDA0_interface {
    Integer createGame(String gameName) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException;
    void clearGamedatabase();
    HashMap<Integer, GameData> getGameDatabase() throws DataAccessException;
}
