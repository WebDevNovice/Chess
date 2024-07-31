package dataaccess.sqlMemory;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GameDAOSQL implements GameDA0Interface {

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createGameTable();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException, ResponseException {
        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Error: Game name cannot be null or empty");
        }
        var statement = "Select game_name from game where game_name = ?";
        List<List<Object>> gameList = UpdateManager.executeQuery(statement, gameName);
        if (!gameList.isEmpty()) {
            throw new DataAccessException("Error: Game name already exists");
        }
        statement = "INSERT INTO game (white_player, black_player, game_name, game_data) VALUES (?, ?, ?, ?)";
        Gson gson = new Gson();
        String game = gson.toJson(new ChessGame());
        Integer gameID = UpdateManager.executeUpdate(statement, null,null, gameName, game);
        return gameID;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException, ResponseException {
        Collection<GameData> games = new ArrayList<>();
        var statement = "SELECT * FROM game";
        List<List<Object>> gameList = UpdateManager.executeQuery(statement);
        for (List<Object> row : gameList) {
            GameData gameData = getGameData(row);
            games.add(gameData);
        }
        return games;
    }

    @Override
    public GameData joinGame(String playerColor, Integer gameId, AuthData authData) throws DataAccessException, BadRequestException, UnvailableTeamException, ResponseException {
        GameData game = null;
        var statement = "Select * From game WHERE id = ?";
        List<List<Object>> gameList = UpdateManager.executeQuery(statement, gameId);
        if (gameList.isEmpty()) {
            throw new DataAccessException("Error: Game ID not found. Try Creating a new one:)");
        }
        if (gameList.size() > 1){
            throw new BadRequestException("Error: Too many games with the same name.");
        }
        for (List<Object> row : gameList) {
            deserializeGame(row);
            if (playerColor == null || playerColor.isEmpty()) {
                throw new BadRequestException("Error: Player color not found. Try Creating a new one:)");
            }
            if (playerColor == "WHITE") {
                setPlayerColor(row, playerColor, authData, 1, gameId);
            }
            else {
                setPlayerColor(row, playerColor, authData, 2, gameId);
            }
        }
        gameList = UpdateManager.executeQuery(statement, gameId);
        game = getGameData(gameList.get(0));
        return game;
    }

    @Override
    public void clearGamedatabase() throws ResponseException, DataAccessException {
        var statement = "DELETE FROM game";
        UpdateManager.executeUpdate(statement);
    }

    @Override
    public HashMap<Integer, GameData> getGameDatabase() throws DataAccessException, ResponseException {
        HashMap<Integer, GameData> gameDataHashMap = new HashMap<>();
        var statement = "SELECT * FROM game";
        List<List<Object>> gameList = UpdateManager.executeQuery(statement);
        for (List<Object> row : gameList) {
            GameData gameData = getGameData(row);
            gameDataHashMap.put(gameData.getGameID(), gameData);
        }
        return gameDataHashMap;
    }

    private ChessGame deserializeGame(List<Object> row) {
        ChessGame game = new Gson().fromJson((String) row.get(4), ChessGame.class);
        return game;
    }

    private void setPlayerColor(List<Object> row, String playerColor, AuthData authData, Integer num, Integer gameID)
            throws UnvailableTeamException, ResponseException, DataAccessException {

        if (row.get(num) == null) {
            String newPlayerColor = playerColor.toLowerCase() + "_player";
            var statement = "UPDATE game SET "+ newPlayerColor +" = ? WHERE id = ?";
            UpdateManager.executeUpdate(statement, authData.getUsername(), gameID);
        } else {
            throw new UnvailableTeamException("Error: Player Color is taken. Try using other player color");
        }
    }
    
    private GameData getGameData(List<Object> row) {
        ChessGame game = deserializeGame(row);
        GameData gameData = new GameData((String) row.get(1), (String) row.get(2), (String) row.get(3),
                game, (Integer) row.get(0));
        return gameData;
    }
}
