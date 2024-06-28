package chess;

public class PawnMoveCalc {

    private final ChessPosition myPosition;
    private final ChessBoard board;

    public PawnMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

}