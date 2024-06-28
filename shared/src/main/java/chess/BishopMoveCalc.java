package chess;

public class BishopMoveCalc {

    private final ChessPosition myPosition;
    private final ChessBoard board;

    public BishopMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

}