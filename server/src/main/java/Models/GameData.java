package Models;

import chess.ChessGame;

public class GameData {
    public int gameID = 1;

    private int uniqueID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    GameData(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        setUniqueID();
        setWhiteUsername(whiteUsername);
        setBlackUsername(blackUsername);
        setGameName(gameName);
        setGame(game);
    }

    private void setUniqueID() {
        GameIDGenerator uniqueIDGenerator = new GameIDGenerator();
        this.uniqueID = uniqueIDGenerator.uniqueID;
    }

    private void setWhiteUsername(String whiteUsername) {
        if (whiteUsername != null) {
            this.whiteUsername = whiteUsername;
        }
        else {
            this.whiteUsername = null;
        }
    }

    private void setBlackUsername(String blackUsername) {
        if (blackUsername != null) {
            this.blackUsername = blackUsername;
        }
        else {
            this.blackUsername = null;
        }
    }

    private void setGameName(String gameName) throws IllegalArgumentException {
        if (gameName != null) {
            this.gameName = gameName;
        }else{
            throw new IllegalArgumentException("Game name cannot be null");
        }
    }

    private void setGame(ChessGame game) throws IllegalArgumentException {
        if (game != null) {
            this.game = game;
        }
        else {
            throw new IllegalArgumentException("Game cannot be null");
        }
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    private class GameIDGenerator{
        private int uniqueID;
        GameIDGenerator(){
            this.uniqueID = GameData.this.gameID++;
            GameData.this.uniqueID++;
        }
    }
}
