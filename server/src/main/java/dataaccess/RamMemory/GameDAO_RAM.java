package dataaccess.RamMemory;

import dataaccess.GameDA0_interface;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GameDAO_RAM implements GameDA0_interface {
    @Override
    public Objects createGame(Objects game) {
        return null;
    }

    @Override
    public Collection<Objects> listGames() {
        return List.of();
    }

    @Override
    public Objects updateGame(Objects game) {
        return null;
    }
}
