package service.wsservices;

import websocket.commands.UserGameCommand;

public class SuperCommand {
    UserGameCommand.CommandType commandType;
    Integer gameID;

    SuperCommand(UserGameCommand.CommandType commandType, Integer gameID) {
        this.commandType = commandType;
        this.gameID = gameID;
    }

    UserGameCommand.CommandType getCommandType() {
        return commandType;
    }

    Integer getGameID() {
        return gameID;
    }
}
