package Services;

import Models.AuthData;
import Models.GameData;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDA0_interface;

import javax.servlet.UnavailableException;
import java.util.Collection;

public class GameServices {
    GameDA0_interface gameDoa;

    public GameServices(GameDA0_interface gameDoa) {
        this.gameDoa = gameDoa;
    }

    public Collection<ChessGame> listGames() throws DataAccessException {
        Collection<ChessGame> games = gameDoa.listGames();
        if (games.isEmpty()) {
            throw new DataAccessException("Error: No games found");
        }
        return games;
    }

    public Integer CreateGame(String gameName) throws DataAccessException {
        for (GameData game : gameDoa.getGameDatabase().values()) {
            if (game.getGameName().equals(gameName)) {
                throw new DataAccessException("Error: Game name already exists");
            }
        }
        return gameDoa.createGame(gameName);
    }

    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException {
        if (gameDoa.getGameDatabase().containsKey(gameId)){
            return gameDoa.joinGame(playerColor, gameId, authData);
        }else{
            throw new BadRequestException("Error: Game does not exist");
        }
    }


}

