package dataaccess;

import Models.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public interface GameDA0_interface {
    Objects createGame(GameDA0_interface game) throws DataAccessException;

    Objects createGame(Objects game) throws DataAccessException;

    Collection<Objects> listGames() throws DataAccessException;
    Objects updateGame(GameDA0_interface game) throws DataAccessException;
    void clearGamedatabase();
    HashMap<Integer, GameData> getGameDatabase() throws DataAccessException;
}
