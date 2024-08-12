package wsfacade.gamehandlers;

import chess.ChessMove;
import model.GameData;
import websocket.commands.UserGameCommand;

import javax.websocket.Session;

public interface GameHandler {
    void updateGame(GameData gameData);
    void printMessage(String message);
    void loadGame(GameData gameData, UserGameCommand command, String username);
    void makeMove(Integer row, String column, String promotionPiece);
    void leaveGame(Session session, UserGameCommand command);
    void resignGame(Session session, UserGameCommand command);
}
