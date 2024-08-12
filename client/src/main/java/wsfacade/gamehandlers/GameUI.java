package wsfacade.gamehandlers;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import ui.BoardCreator;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import wsfacade.WSFacade;

import javax.websocket.Session;

public class GameUI implements GameHandler {
    WSFacade wsFacade;
    ChessGame.TeamColor teamColor;
    GameData currentGame;

    public GameUI(WSFacade wsFacade) {
        this.wsFacade = wsFacade;
    }

    @Override
    public void updateGame(GameData gameData) {
        currentGame = gameData;
        BoardCreator boardCreator = new BoardCreator(currentGame.getGame(), teamColor);
        boardCreator.main();
    }

    @Override
    public void printMessage(String message) {

    }

    @Override
    public void loadGame(GameData gameData, UserGameCommand command, String username) {
        currentGame = gameData;
        if (gameData.getWhiteUsername().equals(username)) {
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else{
            teamColor = ChessGame.TeamColor.BLACK;
        }
        updateGame(gameData);
    }

    @Override
    public void makeMove(Integer row, String column, String promotionPiece) {
        Integer col = colConvertor(column);
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove move = new ChessMove()
    }



    @Override
    public void leaveGame(Session session, UserGameCommand command) {

    }

    @Override
    public void resignGame(Session session, UserGameCommand command) {

    }

    private Integer colConvertor(String column) {
        switch (column) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 3;
            case "D":
                return 4;
            case "E":
                return 5;
            case "F":
                return 6;
            case "G":
                return 7;
            case "H":
                return 8;
            default:
                throw new IllegalArgumentException("Invalid column: " + column);
        }
    }

}
