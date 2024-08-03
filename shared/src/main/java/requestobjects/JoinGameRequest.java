package requestobjects;

public class JoinGameRequest {
    Integer gameID;
    String playerColor;

    public JoinGameRequest(String gameID, String playerColor) {
        this.gameID = Integer.parseInt(gameID);
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }
}
