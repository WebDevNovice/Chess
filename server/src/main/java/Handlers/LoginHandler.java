package Handlers;

import Models.UserData;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class LoginHandler implements Handler {

    @Override
    public Object handle(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
    }
}
