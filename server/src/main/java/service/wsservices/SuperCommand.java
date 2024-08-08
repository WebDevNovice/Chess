package service.wsservices;

import websocket.commands.UserGameCommand;

public class SuperCommand {
    UserGameCommand.CommandType commandType;

    SuperCommand(UserGameCommand.CommandType commandType) {
        this.commandType = commandType;
    }

    UserGameCommand.CommandType getCommandType() {
        return commandType;
    }
}
