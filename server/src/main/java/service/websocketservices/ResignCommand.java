package service.websocketservices;

import websocket.commands.UserGameCommand;

public class ResignCommand extends SuperCommand{
    public ResignCommand(UserGameCommand.CommandType commandType) {
        super(commandType);
    }
}
