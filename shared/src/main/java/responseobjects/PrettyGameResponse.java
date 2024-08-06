package responseobjects;

public class PrettyGameResponse {
    String gameName;
    String whitePlayer;
    String blackPlayer;

    public PrettyGameResponse(String gameName, String whitePlayer, String blackPlayer) {
        this.gameName = gameName;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }
    public String getGameName() {
        return gameName;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    @Override
    public String toString() {
        return "Game Name = " + gameName +
                ", White Player = '" + whitePlayer + '\'' +
                ", Black Player = '" + blackPlayer + '\'' + "\n";
    }
}
