package service.websocketservices;

import websocket.commands.UserGameCommand;

public class ConnectCommand {
    UserGameCommand.CommandType commandType;

    public ConnectCommand(UserGameCommand.CommandType commandType) {
        this.commandType = commandType;
    }
}
