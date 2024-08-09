package service.wsservices;

import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.GameData;
import service.serverservices.GameServices;
import websocket.commands.UserGameCommand;

import java.util.Collection;


public class ResignCommand{
    GameDAOSQL gameDAOSQL;
    GameServices gameServices;
    Integer gameID;
    String playerColor;
    String username;

    public ResignCommand(Integer gameID) {
        gameDAOSQL = new GameDAOSQL();
        this.gameID = gameID;
        this.gameServices = new GameServices(gameDAOSQL);
    }

    public GameData resignGame() throws DataAccessException {
        try{
            Collection<GameData> games = gameServices.listGames();
            for(GameData game: games){
                if(gameID.equals(game.getGameID())){
                   return game;
                }
            }
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Error: Game not found");
    }
}
