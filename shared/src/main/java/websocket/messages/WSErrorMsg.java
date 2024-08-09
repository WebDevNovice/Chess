package websocket.messages;

public class WSErrorMsg extends ServerMessage{
    String message;

    public WSErrorMsg(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
