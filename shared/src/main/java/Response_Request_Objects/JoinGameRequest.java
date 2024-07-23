package Response_Request_Objects;

public class JoinGameRequest {
    String playerColor;
    Integer gameID;

    public JoinGameRequest(String playerColor, Integer gameID) {
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
