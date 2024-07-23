package response_requestobjects;

public class CreateGameRequest {
    String gameName;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }
    public String getGameName() {
        return gameName;
    }
}
