package Response_Request_Objects;


import java.util.Collection;

public class ListGamesResponse <T> {
    Collection<T> games;

    public ListGamesResponse(Collection<T> games) {
        this.games = games;
    }

}
