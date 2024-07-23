package chess.piecemove;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

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


    public List<ChessMove> getValidMoves() {
        List<ChessMove> ValidMoves;
        ValidMoves = new ArrayList<>();

        for (KingPieceMove move : KingPieceMove.values()) {
            int newRow = myPosition.getRow() + move.rowChange;
            int newCol = myPosition.getColumn() + move.colChange;
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessMove newMove = new ChessMove(myPosition,newPosition,null);
            if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { // Bounds Check
                if(board.getPiece(newPosition) == null){
                    ValidMoves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    ValidMoves.add(newMove);
                }
            }
        }
        return ValidMoves;
    }
}
