package chess;

public class QueenMoveCalc {

    private final ChessPosition myPosition;
    private final ChessBoard board;

    public QueenMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

}