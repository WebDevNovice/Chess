package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import responseobjects.CreateGameResponse;
import responseobjects.ListGamesResponse;
import server.Server;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;
import java.util.Random;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }

    @BeforeEach
    public void before() throws ResponseException {
        serverFacade.clear();
    }

    @AfterEach
    public void after() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerSuccess() throws Exception {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        AuthData authData = serverFacade.register(userData);
        Assertions.assertEquals(userData.getUsername(), authData.getUsername());
    }

    @Test
    public void registerFailure() throws ResponseException {
        Assertions.assertThrows(ResponseException.class,() ->
                serverFacade.register(new UserData("Jake","The",null)));
    }

    @Test
    public void loginSuccess() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        serverFacade.register(userData);
        AuthData authData = serverFacade.login(userData);
        Assertions.assertEquals(userData.getUsername(), authData.getUsername());
    }

    @Test
    public void loginFailure() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        serverFacade.register(userData);
        Assertions.assertThrows(ResponseException.class,() ->
                        serverFacade.login(new UserData(name,"Th","Snake")));
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        AuthData authData = serverFacade.register(userData);
        serverFacade.logout(authData);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames(authData));
    }

    @Test
    public void logoutFailure() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        serverFacade.register(userData);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(new AuthData(name, ":)")));
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        AuthData authData = serverFacade.register(userData);
        Assertions.assertInstanceOf(CreateGameResponse.class, serverFacade.createGame("Test123", authData));
    }

    @Test void createGameFailure() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        serverFacade.register(userData);
        AuthData badData = new AuthData(name, ":(");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame("Test123", badData));
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        AuthData authData = serverFacade.register(userData);
        Assertions.assertInstanceOf(ListGamesResponse.class, serverFacade.listGames(authData));
    }

    @Test
    public void listGamesFailure() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        serverFacade.register(userData);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames(new AuthData(name, ":(")));
    }

    @Test
    void joinGameSuccess() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        AuthData authData = serverFacade.register(userData);
        CreateGameResponse newGame = serverFacade.createGame("Test123456789", authData);
        GameData game = serverFacade.joinGame(newGame.getGameID().toString(),"white", authData);
        Assertions.assertInstanceOf(GameData.class,game);
    }

    @Test
    void joinGameFailure() throws ResponseException {
        String name = "J" + new Random().nextInt(1000);
        UserData userData = new UserData(name ,"The","Snake");
        AuthData authData = serverFacade.register(userData);
        CreateGameResponse newGame = serverFacade.createGame("Test12345678910", authData);
        Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.joinGame("1", newGame.getGameID().toString(), authData));
    }
}
