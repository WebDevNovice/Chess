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

    private ChessPosition myPosition;
    private ChessBoard board;

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

        private int rowChange;
        private int colChange;

        KingPieceMove(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }
    }
    public List<ChessPosition> getValidMoves() {
        List<ChessPosition> ValidMoves;
        ValidMoves = new ArrayList<>();

        for (KingPieceMove move : KingPieceMove.values()) {
//            These next two lines of code update the row and column by the enum values for each
//            possible move a king could make
            int newRow = myPosition.getRow() + move.rowChange;
            int newCol = myPosition.getColumn() + move.colChange;
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
/**
 * So I think this should be checking if it is in bounds. I wound if I could check this on the board
 */
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                if(board.getPiece(newPosition) != null){
                    ValidMoves.add(newPosition);
                    System.out.println(newPosition);
                }
//                say they put an invalid line in
                }
            }
        return ValidMoves;
        }
}
