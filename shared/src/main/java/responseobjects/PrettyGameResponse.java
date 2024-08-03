package responseobjects;

public class PrettyGameResponse {
    Integer gameID;
    String whitePlayer;
    String blackPlayer;

    public PrettyGameResponse(Integer gameID, String whitePlayer, String blackPlayer) {
        this.gameID = gameID;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }
    public Integer getGameID() {
        return gameID;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    @Override
    public String toString() {
        return "GameID=" + gameID +
                ", White Player='" + whitePlayer + '\'' +
                ", Black Player='" + blackPlayer + '\'' + "\n";
    }
}
