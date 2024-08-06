package serverfacade;

public class ResponseException extends Exception {
    private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
    }

}
