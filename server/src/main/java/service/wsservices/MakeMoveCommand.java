package service.wsservices;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import model.GameData;
import service.serverservices.GameServices;

import java.util.Collection;

public class MakeMoveCommand {
    Integer gameID;
    GameData gameData;
    ChessMove move;
    String username;
    String playerColor;

    GameDAOSQL gameDAOSQL;
    GameServices gameServices;


    public MakeMoveCommand(Integer gameID, String username, ChessMove move) throws ResponseException, DataAccessException {
        this.gameID = gameID;
        this.username = username;
        this.move = move;

        this.gameDAOSQL = new GameDAOSQL();
        this.gameServices = new GameServices(gameDAOSQL);

        setPlayerColor();
        setGameData();
    }

    //I need get game data method (easy)
    //Also get team color data (easy)

    public GameData makeMove() {
        try{
            if (move == null){
                throw new InvalidMoveException("Error: move is null");
            }
            if (!gameData.getGame().getTeamTurn().toString().equals(playerColor)){
                throw new InvalidMoveException("Error: Not your turn");
            }
            gameData.getGame().makeMove(move);
            GameData updateGame = gameServices.updateGame(gameID,gameData);
            return updateGame;
        } catch (ResponseException | DataAccessException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPlayerColor() throws ResponseException, DataAccessException {
        PlayerColorHelper playerColorHelper = new PlayerColorHelper();
        playerColor = playerColorHelper.setPlayerColor(gameServices, gameID, username);
    }

    public String getPlayerColor() {
        return playerColor;
    }

    private void setGameData() throws ResponseException, DataAccessException {
        Collection<GameData> games = gameServices.listGames();
        for (GameData game : games) {
            if (game.getGameID() == gameID) {
                this.gameData = game;
            }
        }
        if (gameData == null) {
            throw new DataAccessException("Error: Game not found");
        }
    }

    public GameData getGameData() {
        return gameData;
    }
}
