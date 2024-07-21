package Models;

public class JoinGameObject {
    String playerColor;
    Integer gameID;

    public JoinGameObject(String playerColor, Integer gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }
}
