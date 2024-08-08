package service.wsservices;

import websocket.commands.UserGameCommand;

public class LeaveGameCommand extends SuperCommand{

    public LeaveGameCommand(UserGameCommand.CommandType commandType) {
        super(commandType);
    }
}
