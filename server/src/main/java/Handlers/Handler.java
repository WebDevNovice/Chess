package Handlers;

import spark.*;

public interface Handler {
   Object handle(Request request, Response response);
}
