package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * So this needs to store and array of positions
 * that means that I'll need to be able to "visit" each step
 * and check to see if it is valid,
 * if it is valid, append it to my list
 * if not skip
 */
public class KingMoveCalc {

    private final ChessPosition myPosition;
    private final ChessBoard board;
    private final int[][] myArray = new int[8][8];

    public KingMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public enum KingPieceMove {
        UP(-1,0),
        DOWN(1,0),
        LEFT(0,-1),
        RIGHT(0,1),
        DIAONALUPLEFT(-1,-1),
        DIAGONALUPRIGHT(-1,1),
        DIAGONALDOWNLEFT(1,-1),
        DIAGONALDOWNRIGHT(1,1);

        private final int rowChange;
        private final int colChange;

        KingPieceMove(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }
    }
    public List<ChessPosition> getValidMoves() {
        return new ArrayList<>();
    }
}
