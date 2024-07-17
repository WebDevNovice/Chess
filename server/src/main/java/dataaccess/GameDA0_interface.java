package dataaccess;

import dataaccess.RamMemory.GameDAO_RAM;

import java.util.Collection;
import java.util.Objects;

public interface GameDA0_interface {
    Objects createGame(Objects game);
    Collection<Objects> listGames();
    Objects updateGame(Objects game);
}
