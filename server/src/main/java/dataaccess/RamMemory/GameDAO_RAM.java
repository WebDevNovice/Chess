package dataaccess.RamMemory;

import Models.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameDAO_RAM implements GameDA0_interface {
    HashMap<Integer, GameData> gameDataHashMap;

    public GameDAO_RAM() {
        gameDataHashMap = new HashMap<>();
    }

    @Override
    public Objects createGame(GameDA0_interface game) throws DataAccessException {
        return null;
    }

    @Override
    public Objects createGame(Objects game) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<Objects> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public Objects updateGame(GameDA0_interface game) throws DataAccessException {
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
