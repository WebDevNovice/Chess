package responseobjects;


import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesResponse {
    public Collection<GameData> games;


    public ListGamesResponse(Collection<GameData> games) {
        this.games =  games;
    }

    public Collection<GameData> getGames() {
        return games;
    }

    public Collection<PrettyGameResponse> getPrettyGames(){
        Collection<PrettyGameResponse> prettyGames = new ArrayList<>();
        for (GameData game : games) {
            PrettyGameResponse prettyGame = new PrettyGameResponse(game.getGameName(), game.getWhiteUsername(), game.getBlackUsername());
            prettyGames.add(prettyGame);
        }
        return prettyGames;
    }
}
