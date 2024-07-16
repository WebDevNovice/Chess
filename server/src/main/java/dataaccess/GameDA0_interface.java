package dataaccess;

import dataaccess.RamMemory.GameDAO_RAM;

import java.util.Collection;

public interface GameDA0_interface<T> {
    T createGame(T game);
    Collection<T> listGames();
    T updateGame(T game);
}
