package wsfacade.gamehandlers;

import model.GameData;

public interface GameHandler {
    void updateGame(GameData gameData);
    void printMessage(String message);
}
