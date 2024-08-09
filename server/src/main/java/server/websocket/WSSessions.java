package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import service.execeptions.BadRequestException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WSSessions {
    HashMap<Integer, Set<Session>> connections;

    public WSSessions() {
        this.connections = new HashMap<>();
    }

    public void addSession(Integer gameID, Session session) throws BadRequestException {
        if (this.connections.containsKey(gameID)) {
            this.connections.get(gameID).add(session);
        }else {
            connections.put(gameID, new HashSet<>());
            connections.get(gameID).add(session);
        }
    }

    public void removeSession(Integer gameID, Session session) throws BadRequestException {
        try {
            if (!this.connections.containsKey(gameID)) {
                throw new BadRequestException("Session does not exist");
            }
            this.connections.get(gameID).remove(session);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Session> getSession(Integer gameID) throws BadRequestException {
        if (!this.connections.containsKey(gameID)) {
            throw new BadRequestException("Session does not exist");
        }
        return connections.get(gameID);
    }
}
