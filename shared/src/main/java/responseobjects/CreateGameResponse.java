package responseobjects;

public class CreateGameResponse {
    Integer gameID;

    public CreateGameResponse(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
