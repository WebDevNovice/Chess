package ui;

import static ui.EscapeSequences.*;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import responseobjects.CreateGameResponse;
import responseobjects.ListGamesResponse;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Client {
    String visitorName;
    ServerFacade serverFacade;
    String serverUrl;
    Status status;
    AuthData authData;
    Collection<GameData> gameDataList;
    Collection<Integer> gameIDList;

    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
        serverFacade = new ServerFacade(serverUrl);
        status = Status.LOGGEDOUT;
        gameDataList = new ArrayList<>();
        gameIDList = new ArrayList<>();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "watch" -> watchGame(params);
                case "logout" -> logout();
                case "clear" -> clear();
                case "exit" -> "exit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            visitorName = params[0];
            authData = serverFacade.register (new UserData(params[0],params[1],params[2]));
            status = Status.LOGGEDIN;
            return String.format("You are now logged in to %s", visitorName);
        }
        throw new ResponseException(400, "Error: Your are missing your username, password and/or email");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            visitorName = params[0];
            authData = serverFacade.login(new UserData(params[0],params[1],null));
            status = Status.LOGGEDIN;
            return String.format("You are now logged in as %s", visitorName);
        }
        throw new ResponseException(400, "Error: Your username and/or password are incorrect");
    }

    public String logout() throws ResponseException {
            isSignedIn();
            serverFacade.logout(authData);
            status = Status.LOGGEDOUT;
            visitorName = "";
            authData = null;
            return String.format("You are now logged out");
    }

    public String listGames() throws ResponseException {
        isSignedIn();
        Integer num = 1;
        ListGamesResponse games = serverFacade.listGames(authData);
        gameDataList = games.getGames();
        StringBuilder listbuilder = new StringBuilder();
        if (games.getGames().size() == 0) {
            listbuilder.append("No games found");
        }
        for (GameData gameData : games.getGames()) {
            String numStr = String.valueOf(num++);
            String gameName = gameData.getGameName();
            String whitePlayer = gameData.getWhiteUsername();
            String blackPlayer = gameData.getBlackUsername();
            listbuilder.append(numStr + ": Game Name:" + gameName + ", WHITE: " +  whitePlayer + ", BLACK: " + blackPlayer + "\n");
        }
        return listbuilder.toString();
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            isSignedIn();
            CreateGameResponse gameID = serverFacade.createGame(params[0], authData);
            gameIDList.add(gameID.getGameID());
            ListGamesResponse games = serverFacade.listGames(authData);
            gameDataList = games.getGames();
            return String.format("Your newly created gameID is: %s", gameDataList.size());
        }
        throw new ResponseException(400, "Error: We need a game name to proceed, try again");
    }

    public String joinGame(String... params) throws ResponseException {
        ListGamesResponse games = serverFacade.listGames(authData);
        gameDataList = games.getGames();

        if (params.length == 2) {
            int num = 1;
            isSignedIn();
            int index = Integer.parseInt(params[0]);
            Integer gameId = 0;
            for (GameData gameData : gameDataList) {
                if (index == num){
                    gameId = gameData.getGameID();
                }
                else{
                    num++;
                }
            }
            GameData updatedGame = serverFacade.joinGame(gameId.toString(), params[1], authData);
            drawBoard(updatedGame, params[1]);
            return String.format("You are now joining gameID %s as the %s Player", params[0], params[1].toUpperCase());
        }
        throw new ResponseException(400, "Error: We are missing either the game id or the team color you want to join");
    }

    public String watchGame(String... params) throws ResponseException {
        if (params.length == 1) {
            ListGamesResponse games = serverFacade.listGames(authData);
            gameDataList = games.getGames();

            Integer num = 1;
            isSignedIn();
            int index = Integer.parseInt(params[0]);
            GameData selectedGame = null;

            for (GameData gameData : gameDataList) {
                if (index == num){
                    selectedGame = gameData;
                    drawBoard(gameData, params[1]);
                }
                num++;
            }
            if (selectedGame == null) {
                return String.format("No games found");
            }
            return String.format("You are watching %s", selectedGame.getGameName());
        }
        throw new ResponseException(400, "We are missing either the game id or the team color you want to watch");
    }

    public String clear() throws ResponseException {
        serverFacade.clear();
        return "You successfully avoided nuclear war:)";
    }

    public String help() {
        if (status == Status.LOGGEDOUT) {
            return """
                    [LOGGED_OUT] Please type in one of the following commands:\n
                    - register <username> <password> <email>
                    - login <username> <password>
                    - exit
                    - help
                    """;
        }
        return """
                [LOGGED_IN] Please type in one of the following commands:\n
                - list
                - create <gameName>
                - join <gameID> <WHITE/BLACK> ~ denotes team color you will play as
                - watch <gameID> <WHITE/BLACK> ~ denotes team color you will watch
                - logout
                - exit
                - help
                """;
    }

    private void isSignedIn() throws ResponseException {
        if (status == Status.LOGGEDOUT) {
            throw new ResponseException(400, SET_TEXT_BOLD+ SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "Error: Must be signed in!");
        }
    }

    private void drawBoard(GameData updatedGame, String teamColor) {
        if (teamColor.toUpperCase().equals("WHITE")){
            BoardCreator boardCreator = new BoardCreator(updatedGame.getGame(), ChessGame.TeamColor.WHITE);
            boardCreator.main();
        }
        else{
            BoardCreator boardCreator = new BoardCreator(updatedGame.getGame(), ChessGame.TeamColor.BLACK);
            boardCreator.main();
        }
    }
}
