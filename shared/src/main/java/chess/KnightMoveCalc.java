package chess;

public class KnightMoveCalc {

    private final ChessPosition myPosition;
    private final ChessBoard board;

    public KnightMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

}