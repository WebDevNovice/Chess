package response_request_objects;

public class CreateGameRequest {
    String gameName;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }
    public String getGameName() {
        return gameName;
    }
}
