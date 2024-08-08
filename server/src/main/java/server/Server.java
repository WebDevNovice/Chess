package server;
import dataaccess.sqlMemory.AuthDAOSQL;
import dataaccess.sqlMemory.GameDAOSQL;
import dataaccess.sqlMemory.ResponseException;
import dataaccess.sqlMemory.UserDAOSQL;
import model.*;
import requestobjects.CreateGameRequest;
import responseobjects.CreateGameResponse;
import requestobjects.JoinGameRequest;
import responseobjects.ErrorMessage;
import responseobjects.ListGamesResponse;
import com.google.gson.Gson;
import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDA0Interface;
import dataaccess.UserDaoInterface;
import server.websocket.WSHandler;
import service.execeptions.BadRequestException;
import service.execeptions.UnvailableTeamException;
import service.serverservices.AuthServices;
import service.serverservices.ClearService;
import service.serverservices.GameServices;
import service.serverservices.UserServices;
import spark.*;

import java.util.ArrayList;
import java.util.Collection;

public class Server {
    UserDaoInterface userDao;
    AuthDAOInterface authDao;
    GameDA0Interface gameDao;
    public UserServices userServices;
    public AuthServices authServices;
    public GameServices gameServices;
    public ClearService clearService;
    public Gson gson = new Gson();

    private final WSHandler wsHandler;

    public Server() {
        this.userDao = new UserDAOSQL();
        this.authDao = new AuthDAOSQL();
        this.gameDao = new GameDAOSQL();

        this.userServices = new UserServices(userDao, authDao);
        this.authServices = new AuthServices(authDao);
        this.gameServices = new GameServices(gameDao);
        this.clearService = new ClearService(userDao, authDao, gameDao);

        this.wsHandler = new WSHandler(this.authServices);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // register your endpoints and handle exceptions here.

        Spark.delete("/db",this::clear);
        Spark.post("/user",this::register);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.webSocket("/ws", wsHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) {

        try {
            var user = gson.fromJson(req.body(), UserData.class);
            AuthData authToken = userServices.register(user);
            return successResponseAuthToken(gson, authToken, res);

        } catch (DataAccessException e) {
            return errorResponse(400, gson,e.getMessage(), res);
        }catch (BadRequestException e) {
            return errorResponse(403, gson,e.getMessage(), res);
        }
        catch (Exception e) {
            return errorResponse(500,gson,"Internal server error",res);
        }
    }

    private Object login(Request req, Response res) {

        try {
            var user = gson.fromJson(req.body(), UserData.class);
            AuthData authToken = userServices.login(user);
            return successResponseAuthToken(gson, authToken, res);

        } catch (DataAccessException e) {
            return errorResponse(401, gson, e.getMessage(), res);
        }catch (ResponseException e){
            return errorResponse(e.getStatusCode(), gson, e.getMessage(), res);
        } catch (Exception e) {
            return errorResponse(500,gson,e.getMessage(),res);
        }
    }

    private Object logout(Request req, Response res) {
        try {
            AuthData authtoken = authServices.isAuthenticated(req);
            authServices.logout(authtoken.getAuthToken());
            res.status(200);
            return gson.toJson(null);


        } catch (DataAccessException e) {
            return errorResponse(401, gson, e.getMessage(), res);
        }

        catch (BadRequestException e){
            return errorResponse(400, gson, e.getMessage(), res);
        }

        catch (Exception e) {
            return errorResponse(500,gson,e.getMessage(),res);
        }
    }

    private Object listGames(Request req, Response res) {
        try{
            authServices.isAuthenticated(req);
            Collection<GameData> games = gameServices.listGames();
            ListGamesResponse listofGames = new ListGamesResponse(games);
            if (listofGames.games == null){
                listofGames.games = new ArrayList<>();
            }
            res.status(200);
            return gson.toJson(listofGames);

        } catch (DataAccessException e) {
            return errorResponse(401, gson, e.getMessage(), res);
        }
        catch (Exception e){
            return errorResponse(500,gson,"Internal server error",res);
        }
    }

    private Object createGame(Request req, Response res) {
        try {
            authServices.isAuthenticated(req);
            CreateGameRequest gameName = gson.fromJson(req.body(), CreateGameRequest.class);
            Integer gameID = gameServices.createGame(gameName.getGameName());
            CreateGameResponse createGameResponse = new CreateGameResponse(gameID);
            res.status(200);
            return gson.toJson(createGameResponse);


        } catch (DataAccessException e) {
            return errorResponse(401, gson, e.getMessage(), res);

        } catch (BadRequestException e){
            return errorResponse(400, gson, e.getMessage(), res);

        } catch (Exception e){
            return errorResponse(500,gson,"Internal server error",res);
        }
    }

    private Object joinGame(Request req, Response res) {
        try {
            AuthData authToken = authServices.isAuthenticated(req);
            JoinGameRequest playerData = gson.fromJson(req.body(), JoinGameRequest.class);
            GameData updatedGame = gameServices.joinGame(playerData.getPlayerColor(), playerData.getGameID(), authToken);
            wsHandler.onMessage();
            res.status(200);
            return gson.toJson(updatedGame);

        } catch (DataAccessException e) {

            return errorResponse(401, gson, e.getMessage(), res);

        } catch (BadRequestException e){
            return errorResponse(400, gson, e.getMessage(), res);
        }

        catch (UnvailableTeamException e) {

            return errorResponse(403, gson, e.getMessage(), res);

        } catch (Exception e) {
            return errorResponse(500, gson, "Internal server error", res);
        }
    }

    private Object clear(Request req, Response res) {
        try {
            clearService.clearAllDatabases();

            if (clearService.getUserDao().getUserDatabase().isEmpty() &&
                clearService.getAuthDao().getAuthDatabase().isEmpty() &&
                clearService.getGameDao().getGameDatabase().isEmpty()){
                res.status(200);
                return gson.toJson(null);

            }

        } catch (Exception e) {
            return errorResponse(500,gson,e.getMessage(),res);
        }
        return null;
    }

    //the following are methods that stay in the scope of this class

    private String successResponseAuthToken(Gson gson, AuthData authToken, Response res) {
        res.status(200);
        return gson.toJson(authToken);
    }

    private String errorResponse(Integer statusCode, Gson gson, String errorMessage, Response res) {
        res.status(statusCode);
        ErrorMessage error = new ErrorMessage(errorMessage);
        String errorJson = gson.toJson(error);
        res.body(errorJson);
        return errorJson;
    }
}



