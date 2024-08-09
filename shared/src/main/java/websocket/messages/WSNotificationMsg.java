package websocket.messages;

public class WSNotificationMsg extends ServerMessage{
    String message;

    public WSNotificationMsg(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
