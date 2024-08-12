package service.wsservices;

import chess.ChessGame;
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


    public ResignCommand(Integer gameID, String username) throws ResponseException, DataAccessException {
        gameDAOSQL = new GameDAOSQL();
        this.gameID = gameID;
        this.gameServices = new GameServices(gameDAOSQL);
        this.username = username;
        PlayerColorHelper playerColorHelper = new PlayerColorHelper();
        this.playerColor = playerColorHelper.setPlayerColor(gameServices, gameID, username);
    }

    public void resign() throws ResponseException, DataAccessException {
        Collection<GameData> games = gameServices.listGames();
        for(GameData game: games){
            if (game.getGameID() == this.gameID){
                game.getGame().resignGame();
                gameServices.updateGame(gameID, game);
            }
        }
    }

    public boolean isPlayer() throws DataAccessException, ResponseException {
        if (playerColor == null) {
            return false;
        }
        return true;
    }

    public boolean isGameOver() throws ResponseException, DataAccessException {
        Collection<GameData> games = gameServices.listGames();
        ChessGame.TeamColor teamColor = null;
        for (GameData gameData : games) {
            if (gameData.getGameID() == gameID) {
                if(playerColor == "WHITE"){
                    teamColor = ChessGame.TeamColor.WHITE;
                }
                else {
                     teamColor = ChessGame.TeamColor.BLACK;
                }
                return gameData.getGame().isGameOver(teamColor);
            }
        }
        return false;
    }
}
