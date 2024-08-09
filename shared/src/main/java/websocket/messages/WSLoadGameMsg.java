package websocket.messages;

import model.GameData;

public class WSLoadGameMsg extends ServerMessage {
    GameData gameData;

    public WSLoadGameMsg(ServerMessageType type, GameData gameData) {
        super(type);
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return gameData;
    }
}
