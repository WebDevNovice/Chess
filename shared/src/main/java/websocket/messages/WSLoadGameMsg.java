package websocket.messages;

import model.GameData;

public class WSLoadGameMsg extends ServerMessage {
    GameData game;

    public WSLoadGameMsg(ServerMessageType type, GameData gameData) {
        super(type);
        this.game = gameData;
    }

    public GameData getGameData() {
        return game;
    }
}
