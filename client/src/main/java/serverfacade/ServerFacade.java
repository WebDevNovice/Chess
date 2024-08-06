package serverfacade;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import requestobjects.CreateGameRequest;
import requestobjects.JoinGameRequest;
import responseobjects.CreateGameResponse;
import responseobjects.ListGamesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

/**
 * Spark.delete("/db",this::clear);
 * Spark.post("/user",this::register);
 * Spark.post("/session",this::login);
 * Spark.delete("/session",this::logout);
 * Spark.get("/game",this::listGames);
 * Spark.post("/game",this::createGame);
 * Spark.put("/game", this::joinGame);
 */


public class ServerFacade {
    String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData register(UserData userData) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public AuthData login(UserData userData) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public void logout(AuthData authData) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null,null, authData);
    }

    public ListGamesResponse listGames(AuthData authData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResponse.class, authData);
    }

    public CreateGameResponse createGame(String gameName, AuthData authData) throws ResponseException {
        var path = "/game";
        CreateGameRequest request = new CreateGameRequest(gameName);
        return this.makeRequest("POST", path, request, CreateGameResponse.class, authData);
    }

    public GameData joinGame(String gameID, String PlayerColor, AuthData authData) throws ResponseException {
        var path = "/game";
        JoinGameRequest request = new JoinGameRequest(gameID, PlayerColor.toUpperCase());
        return this.makeRequest("PUT", path, request, GameData.class, authData);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE",path,null,null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData headerData) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (headerData != null){
                writeHeader(headerData, http);
            }
            writeBody(request, http);

            http.connect();

            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(AuthData header, HttpURLConnection http) {
        http.addRequestProperty("Authorization", header.getAuthToken());
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
