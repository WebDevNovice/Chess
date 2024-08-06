package ui;

import static ui.EscapeSequences.*;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import requestobjects.JoinGameRequest;
import responseobjects.CreateGameResponse;
import responseobjects.ListGamesResponse;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Collection;

public class Client {
    String visitorName;
    ServerFacade serverFacade;
    String serverUrl;
    Status status;
    AuthData authData;

    public Client(String serverUrl) {
        this.serverUrl = serverUrl;
        serverFacade = new ServerFacade(serverUrl);
        status = Status.LOGGEDOUT;
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
        throw new ResponseException(400, "Error: Your are missing parameters");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            visitorName = params[0];
            authData = serverFacade.login(new UserData(params[0],params[1],null));
            status = Status.LOGGEDIN;
            return String.format("You are now logged in as %s", visitorName);
        }
        throw new ResponseException(400, "Error: Incorrect Number of Parameters");
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
        ListGamesResponse games = serverFacade.listGames(authData);
        return String.format(games.getPrettyGames().toString());
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            isSignedIn();
            CreateGameResponse gameID = serverFacade.createGame(params[0], authData);
            return String.format("Your newly created gameID is: %s", gameID.getGameID());
        }
        throw new ResponseException(400, "Error: Incorrect Number of Parameters");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            isSignedIn();
            GameData updatedGame = serverFacade.joinGame(params[0], params[1], authData);
            if (params[1].toUpperCase().equals("WHITE")){
                BoardCreator boardCreator = new BoardCreator(updatedGame.getGame(), ChessGame.TeamColor.WHITE);
                boardCreator.main();
            }
            else{
                BoardCreator boardCreator = new BoardCreator(updatedGame.getGame(), ChessGame.TeamColor.BLACK);
                boardCreator.main();
            }

            return String.format("You are now joining gameID %s as the %s Player", params[0], params[1].toUpperCase());
        }
        throw new ResponseException(400, "Error: Incorrect Number of Parameters");
    }

    public String watchGame(String... params) throws ResponseException {
        if (params.length == 2) {
            isSignedIn();
            GameData updatedGame = serverFacade.joinGame(params[0], params[1], authData);
            if (params[1].toUpperCase().equals("WHITE")){
                BoardCreator boardCreator = new BoardCreator(updatedGame.getGame(), ChessGame.TeamColor.WHITE);
                boardCreator.main();
            }
            else{
                BoardCreator boardCreator = new BoardCreator(updatedGame.getGame(), ChessGame.TeamColor.BLACK);
                boardCreator.main();
            }

            return String.format("You are now watching gameID %s from the perspective of the %s Player", params[0], params[1].toUpperCase());
        }
        throw new ResponseException(400, "Error: Incorrect Number of Parameters");
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
}
