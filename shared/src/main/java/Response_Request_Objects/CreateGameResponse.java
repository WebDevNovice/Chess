package Response_Request_Objects;

public class CreateGameResponse {
    Integer gameID;

    public CreateGameResponse(Integer gameID) {
        this.gameID = gameID;
    }
    public Integer getGameID() {
        return gameID;
    }
}
