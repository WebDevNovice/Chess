import chess.*;
import server.Server;

public class Main {

    private static void testServer(){
        int desiredPort = 8080;
        Server server = new Server();
        server.run(desiredPort);
    }

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        testServer();
    }
}