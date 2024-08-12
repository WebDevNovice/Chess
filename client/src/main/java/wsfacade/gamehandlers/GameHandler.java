package wsfacade.gamehandlers;

import chess.ChessMove;
import chess.ChessPiece;
import model.GameData;
import websocket.commands.UserGameCommand;

import javax.websocket.Session;

public interface GameHandler {
    void updateGame(GameData gameData);
    void printMessage(String message);
    void loadGame(GameData gameData, UserGameCommand command, String username);
    ChessMove makeMove(Integer sRow, String sColumn, Integer eRow, String eColumn, ChessPiece.PieceType promotionPiece);
    UserGameCommand leaveGame(UserGameCommand command);
    UserGameCommand resignGame(UserGameCommand command);
}
