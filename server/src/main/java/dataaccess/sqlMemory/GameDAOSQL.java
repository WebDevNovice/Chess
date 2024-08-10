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

    public GameDAOSQL(){
        try{
            DatabaseManager.createDatabase();
            DatabaseManager.createGameTable();
        }catch (Exception e){
            e.printStackTrace();
        }
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
            if (playerColor.equals("WHITE")) {
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

    private void setPlayerColor(List<Object> row, String playerColor, AuthData authData, Integer num, Integer gameID)
            throws UnvailableTeamException, ResponseException, DataAccessException {

        if (row.get(num) == null || row.get(num).equals("")) {
            String newPlayerColor = playerColor.toLowerCase() + "_player";
            var statement = "UPDATE game SET "+ newPlayerColor +" = ? WHERE id = ?";
            UpdateManager.executeUpdate(statement, authData.getUsername(), gameID);
        } else {
            throw new UnvailableTeamException("Error: Player Color is taken. Try using other player color");
        }
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

    @Override
    public GameData updateGame(Integer gameID, GameData updatedGame) throws ResponseException, DataAccessException {
        var statement = "Update game SET game_data = ? WHERE id = ?";
        String updatedJsonGame = new Gson().toJson(updatedGame.getGame());
        UpdateManager.executeUpdate(statement, updatedJsonGame, gameID);
        //This is essentially just checking that I am implementing this correctly
        Collection<GameData> games = listGames();
        for (GameData game: games){
            if (game.getGameID() == gameID){
                if (game.getGame().equals( updatedGame.getGame())){
                    return game;
                }
            }
        }
        throw new DataAccessException("Error: Game was not updated");
    }

    public GameData updatePlayer(Integer gameID, String playerColor, String updatedUsername) throws ResponseException, DataAccessException {
        if (playerColor == null || playerColor.isEmpty()) {
            throw new DataAccessException("Error: Player color required.");
        }
        if (gameID == null || gameID < 1) {
            throw new DataAccessException("Error: Game ID is required.");
        }
        if (playerColor.equals("WHITE")) {
            var statement = "UPDATE game SET white_player = ? WHERE id = ?";
            GameData gameData = updatePlayerHelper(statement, updatedUsername, gameID);
            return gameData;
        }
        else {
            var statement = "UPDATE game SET black_player = ? WHERE id = ?";
            GameData gameData = updatePlayerHelper(statement, updatedUsername, gameID);
            return gameData;
        }
    }

    private GameData updatePlayerHelper(String statement, String updatedUsername, Integer gameID) throws DataAccessException, ResponseException {
        Integer gameId = UpdateManager.executeUpdate(statement, updatedUsername, gameID);
        getGameDataList(gameId);

        Collection<GameData> games = listGames();
        for (GameData game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Error: Game was deleted somehow");
    }

    private ChessGame deserializeGame(List<Object> row) {
        ChessGame game = new Gson().fromJson((String) row.get(4), ChessGame.class);
        return game;
    }
    
    private GameData getGameData(List<Object> row) {
        ChessGame game = deserializeGame(row);
        GameData gameData = new GameData((String) row.get(1), (String) row.get(2), (String) row.get(3),
                game, (Integer) row.get(0));
        return gameData;
    }

    private List<List<Object>> getGameDataList(Integer gameID) throws ResponseException, DataAccessException {
        String statement = "SELECT * FROM game WHERE id = ?";
        List<List<Object>> gameList = UpdateManager.executeQuery(statement, gameID);
        return gameList;
    }



}
