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
    public Objects createGame(Objects game) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<Objects> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public Objects updateGame(Objects game) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteGamedatabase() throws DataAccessException {
        if (!gameDataHashMap.isEmpty()) {
            gameDataHashMap.clear();
        }
        else{
            throw new DataAccessException("Game data map is empty");
        }
    }
}
