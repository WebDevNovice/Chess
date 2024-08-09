package websocket.messages;

public class WSErrorMsg extends ServerMessage{
    String errorMessage;

    public WSErrorMsg(ServerMessageType type, String message) {
        super(type);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
