package service.wsservices;

import websocket.commands.UserGameCommand;

public class MakeMoveCommand extends SuperCommand {
    public MakeMoveCommand(UserGameCommand.CommandType commandType) {
        super(commandType);
    }
}
