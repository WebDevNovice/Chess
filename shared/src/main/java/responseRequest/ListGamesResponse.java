package responseRequest;


import java.util.Collection;

public class ListGamesResponse <T> {
    public Collection<T> games;

    public ListGamesResponse(Collection<T> games) {
        this.games = games;
    }

}
