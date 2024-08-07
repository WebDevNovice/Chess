package service.websocketservices;

import websocket.commands.UserGameCommand;

public class ConnectCommand extends SuperCommand {

    public ConnectCommand(UserGameCommand.CommandType commandType) {
        super(commandType);
    }

}
