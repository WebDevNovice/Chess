package Services;

import Models.GameData;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import java.util.HashMap;

public class GameServices {
    GameDA0_interface gameDoa;

    public GameServices(GameDA0_interface gameDoa) {
        this.gameDoa = gameDoa;
    }

    HashMap<Integer, GameData> listGames(){
        return null;
    }

    public Integer CreateGame(String gameName) throws DataAccessException {
        return gameDoa.createGame(gameName);
    }

    GameData joinGame(String teamColor, Integer gameID ){
        return null;
    }
}
