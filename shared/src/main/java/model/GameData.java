package model;

import chess.ChessGame;

public class GameData {
    public Integer gameID;

    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public GameData(String whiteUsername, String blackUsername, String gameName, ChessGame game, Integer gameID) {
        setWhiteUsername(whiteUsername);
        setBlackUsername(blackUsername);
        setGameName(gameName);
        setGame(game);
        setGameID(gameID);
    }

    public void setWhiteUsername(String whiteUsername) {
        if (whiteUsername != null) {
            this.whiteUsername = whiteUsername;
        }
        else {
            this.whiteUsername = null;
        }
    }

    public void setBlackUsername(String blackUsername) {
        if (blackUsername != null) {
            this.blackUsername = blackUsername;
        }
        else {
            this.blackUsername = null;
        }
    }

    private void setGameName(String gameName) {
        this.gameName = gameName;
    }

    private void setGame(ChessGame game)  {
        this.game = game;
    }

    private void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
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

}
