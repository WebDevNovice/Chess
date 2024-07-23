package Response_Request_Objects;

import chess.ChessGame;

import java.util.Collection;

public class ListGamesResponse {
    Collection<ChessGame> games;

    public ListGamesResponse(Collection<ChessGame> games) {
        this.games = games;
    }

    public Collection<ChessGame> getGames() {
        return games;
    }
}
