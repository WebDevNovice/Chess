package ui;

import static ui.EscapeSequences.*;

import chess.ChessGame;
import chess.ChessPiece;
import model.AuthData;
import model.GameData;
import model.UserData;
import responseobjects.CreateGameResponse;
import responseobjects.ListGamesResponse;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import wsfacade.WSFacade;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Client {
    String visitorName;
    ServerFacade serverFacade;
    WSFacade wsFacade;
    String serverUrl;
    Status status;
    AuthData authData;
    Collection<GameData> gameDataList;
    Collection<Integer> gameIDList;
    Integer gameID;

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
                case "make move", "mm" -> makeMove(params);
                case "highlight legal moves", "hlm" -> highlightLegalMove(params);
                case "redraw board", "rdb" -> redrawBoard(params);
                case "leave", "l" -> leaveGame();
                case "resign", "r" -> resignGame();
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
            this.gameID = gameId;
            GameData updatedGame = serverFacade.joinGame(gameId.toString(), params[1], authData);
            status = Status.INGAME;
            String message = String.format("\nYou are now joining gameID %s as the %s Player", params[0], params[1].toUpperCase());
            System.out.println(message);

            connectToWS(updatedGame);

            return "";
        }
        throw new ResponseException(400, "Error: We are missing either the game id or the team color you want to join");
    }

    public String watchGame(String... params) throws ResponseException {
        if (params.length >= 1 && status == Status.INGAME) {

            ListGamesResponse games = serverFacade.listGames(authData);
            gameDataList = games.getGames();

            Integer num = 1;
            isSignedIn();
            int index = Integer.parseInt(params[0]);
            GameData selectedGame = null;

            for (GameData gameData : gameDataList) {
                if (index == num){
                    selectedGame = gameData;
                    drawBoard(gameData, "");
                }
                num++;
            }
            if (selectedGame == null) {
                return String.format("No games found");
            }
            this.gameID = selectedGame.getGameID();
            connectToWS(selectedGame);

            status = Status.INGAME;
            String message = String.format("You are watching %s \n", selectedGame.getGameName());
            System.out.println(message);

            return "";
        }
        throw new ResponseException(400, "We are missing either the game id");
    }

    public String makeMove(String... params) throws ResponseException {
    //(Start Position) <row, col> (End Position) <row, col>  <*promotion piece*> ~ ** indicates optional
        if(params.length == 4 && status == Status.INGAME){
            int sRow;
            String sCol;
            int eRow;
            String eCol;
            String promotionPiece;
            ChessPiece.PieceType chessPiece = null;
            try{
                if (params.length == 4){
                    sRow = Integer.parseInt(params[0]);
                    sCol = params[1];
                    eRow = Integer.parseInt(params[2]);
                    eCol = params[3];
                } else if (params.length == 5) {
                    sRow = Integer.parseInt(params[0]);
                    sCol = params[1];
                    eRow = Integer.parseInt(params[2]);
                    eCol = params[3];
                    promotionPiece = params[4];
                    chessPiece = parsePieceType(promotionPiece);
                }
                else{
                    throw new ResponseException(400, "Error: Invalid Move Input");
                }
            } catch (NumberFormatException | ResponseException e) {
                throw new RuntimeException(e);
            }

            try{
                wsFacade.makeMove(gameID, sRow, sCol, eRow, eCol, chessPiece);
                return "";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new ResponseException(400, "Error: Invalid Move Input");
    }

    public String highlightLegalMove(String... params) throws ResponseException {
        //- highlight legal moves / hlm: (Start Position) <row, col>}
        return "";
    }

    public String redrawBoard(String... params) throws ResponseException {
        this.gameID = Integer.parseInt(params[0]);
        for (GameData gameData : gameDataList) {
            if (gameData.getGameID() == gameID){
                if (gameData.getWhiteUsername().equals(authData.getUsername())){
                    String teamColor = "WHITE";
                    drawBoard(gameData, teamColor);
                }
                else {
                    String teamColor = "BLACK";
                    drawBoard(gameData, teamColor);
                }
            }
        }
        return "";
    }

    public String leaveGame() throws ResponseException {
        try {
            wsFacade.leaveGame();
            status = Status.LOGGEDIN;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public String resignGame() throws ResponseException {
        try {
        wsFacade.resignGame();
        status = Status.LOGGEDIN;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
        return "";}

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
        } else if (status == Status.LOGGEDIN) {
            return """
                [LOGGED_IN] Please type in one of the following commands:\n
                - list
                - create <gameName>
                - join <gameID> <WHITE/BLACK> ~ denotes team color you will play as
                - watch <gameID> 
                - logout
                - exit
                - help
                """;
        }
        else{
            return """
                   [In_Game] Please type in one of the following commands:\n
                   - make move / mm (Players Only): (Start Position) <row, col> (End Position) <row, col>  <*promotion piece*> ~ ** indicates optional
                   - highlight legal moves / hlm: (Start Position) <row, col>
                   - redraw board / rdb
                   - leave / l
                   - resign (Players Only) / r
                   - help 
                    """;
        }
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

    private void connectToWS(GameData gameData) {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                authData.getAuthToken(), gameData.getGameID(), null);
        wsFacade = new WSFacade(serverUrl, command, authData.getUsername());

        try{
            wsFacade.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ChessPiece.PieceType parsePieceType(String pieceType) {
        switch (pieceType) {
            case "King", "K", "k":
                return ChessPiece.PieceType.KING;
            case "Queen", "Q", "q":
                return ChessPiece.PieceType.QUEEN;
            case "Knight", "N", "n":
                return ChessPiece.PieceType.KNIGHT;
            case "Bishop", "B", "b":
                return ChessPiece.PieceType.BISHOP;
            case "Rook", "R", "r":
                return ChessPiece.PieceType.ROOK;
            case "Pawn", "P", "p":
                return ChessPiece.PieceType.PAWN;
            default:
                throw new RuntimeException("Invalid piece type: " + pieceType);
        }
    }

}
