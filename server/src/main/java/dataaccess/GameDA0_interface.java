package dataaccess;

import java.util.Collection;
import java.util.Objects;

public interface GameDA0_interface {
    Objects createGame(Objects game) throws DataAccessException;
    Collection<Objects> listGames() throws DataAccessException;
    Objects updateGame(Objects game) throws DataAccessException;
    void deleteGamedatabase() throws DataAccessException;
}
