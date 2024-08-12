package wsfacade.gamehandlers;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.BoardCreator;
import websocket.commands.UserGameCommand;
import wsfacade.WSFacade;

import javax.websocket.Session;

import static ui.EscapeSequences.*;

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
        if (wsFacade.getUsername().equals(gameData.getWhiteUsername())){
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if (wsFacade.getUsername().equals(gameData.getBlackUsername())){
            teamColor = ChessGame.TeamColor.BLACK;
        }
        BoardCreator boardCreator = new BoardCreator(currentGame.getGame(), teamColor);
        boardCreator.main();
    }

    @Override
    public void printMessage(String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_TEXT_COLOR_RED);
        stringBuilder.append(message);
        stringBuilder.append(RESET_TEXT_COLOR);

        System.out.println(stringBuilder.toString());
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
    public ChessMove makeMove(Integer sRow, String sColumn, Integer eRow, String eColumn, ChessPiece.PieceType promotionPiece) {
        Integer sCol = colConvertor(sColumn);
        Integer eCol = colConvertor(eColumn);

        ChessPosition sPosition = new ChessPosition(sRow, sCol);
        ChessPosition ePosition = new ChessPosition(eRow, eCol);
        ChessMove move = new ChessMove(sPosition, ePosition, promotionPiece);
        return move;
    }

    @Override
    public void leaveGame(Session session, UserGameCommand command) {

    }

    @Override
    public void resignGame(Session session, UserGameCommand command) {

    }

    private Integer colConvertor(String column) {
        switch (column) {
            case "A","a":
                return 1;
            case "B","b":
                return 2;
            case "C", "c":
                return 3;
            case "D", "d":
                return 4;
            case "E", "e":
                return 5;
            case "F","f":
                return 6;
            case "G","g":
                return 7;
            case "H","h":
                return 8;
            default:
                throw new IllegalArgumentException("Invalid column: " + column);
        }
    }

}
