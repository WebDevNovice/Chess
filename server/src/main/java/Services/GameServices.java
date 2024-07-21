package Services;

import Models.AuthData;
import Models.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import java.util.Collection;
import java.util.HashMap;

public class GameServices {
    GameDA0_interface gameDoa;

    public GameServices(GameDA0_interface gameDoa) {
        this.gameDoa = gameDoa;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return gameDoa.listGames();
    }

    public Integer CreateGame(String gameName) throws DataAccessException {
        return gameDoa.createGame(gameName);
    }

    public GameData joinGame(String teamColor, Integer gameID, AuthData authData) throws DataAccessException {
        return gameDoa.joinGame(teamColor, gameID, authData);
    }

}
