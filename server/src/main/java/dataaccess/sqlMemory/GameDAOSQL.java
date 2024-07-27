package dataaccess.sqlMemory;

import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import model.AuthData;
import model.GameData;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GameDAOSQL implements GameDA0Interface {
    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException {
        return null;
    }

    @Override
    public void clearGamedatabase() {

    }

    @Override
    public HashMap<Integer, GameData> getGameDatabase() throws DataAccessException {
        return null;
    }
}
