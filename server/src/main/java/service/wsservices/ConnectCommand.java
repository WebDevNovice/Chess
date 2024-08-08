package service.wsservices;

import websocket.commands.UserGameCommand;

public class ConnectCommand extends SuperCommand {

    public ConnectCommand(UserGameCommand.CommandType commandType) {
        super(commandType);
    }

}
