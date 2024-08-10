package service.wsservices;

import dataaccess.DataAccessException;
import dataaccess.sqlMemory.ResponseException;
import model.GameData;
import service.serverservices.GameServices;

import java.util.Collection;

public class PlayerColorHelper {

    public String setPlayerColor(GameServices gameServices, Integer gameID, String username) throws ResponseException, DataAccessException {
        Collection<GameData> games = gameServices.listGames();
        for (GameData game: games) {
            if (game.getGameID() == gameID){

                if (game.getWhiteUsername() == null || game.getBlackUsername() == null){
                    return null;
                }
                if (game.getBlackUsername().equals(username)){
                    return "BLACK";
                }
                if (game.getWhiteUsername().equals(username)){
                    return "WHITE";
                }
            }
        }
        return null;//this is the case when an observer has joined
    }
}
