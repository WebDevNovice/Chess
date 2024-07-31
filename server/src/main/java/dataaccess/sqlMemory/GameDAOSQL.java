package dataaccess.sqlMemory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import model.AuthData;
import model.GameData;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GameDAOSQL implements GameDA0Interface {
    @Override
    public Integer createGame(String gameName) throws DataAccessException, ResponseException {
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Error: Game name cannot be null or empty");
        }
        var statement = "INSERT INTO game (white_player, black_player, game_name, game_data) VALUES (?, ?, ?, ?)";
        Integer gameID = UpdateManager.executeUpdate(statement, "","", gameName, new ChessGame());
        return gameID;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException, ResponseException {
        Collection<GameData> games = new ArrayList<>();
        var statement = "SELECT * FROM game";
        List<List<Object>> gameList = UpdateManager.executeQuery(statement);
        for (List<Object> row : gameList) {
            GameData gameData = new GameData((String) row.get(1), (String) row.get(2), (String) row.get(3),
                                             (ChessGame) row.get(4), (Integer) row.get(0));
            games.add(gameData);
        }
        return games;
    }

    @Override
    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException {
        return null;
    }

    @Override
    public void clearGamedatabase() throws ResponseException, DataAccessException {
        var statement = "DELETE FROM game";
        UpdateManager.executeUpdate(statement);
    }

    @Override
    public HashMap<Integer, GameData> getGameDatabase() throws DataAccessException {
        return null;
    }
}
