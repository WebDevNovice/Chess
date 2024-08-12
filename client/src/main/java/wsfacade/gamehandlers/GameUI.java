package wsfacade.gamehandlers;

import chess.ChessGame;
import model.GameData;
import ui.BoardCreator;
import websocket.messages.ServerMessage;
import wsfacade.WSFacade;

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

    public void loadGame(ServerMessage serverMessage){}

    public void makeMove(ServerMessage serverMessage){}

    public void leaveGame(ServerMessage serverMessage){}

    public void resignGame(ServerMessage serverMessage){}

}
