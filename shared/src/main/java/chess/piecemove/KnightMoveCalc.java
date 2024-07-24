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
public class KnightMoveCalc {

    private ChessPosition myPosition;
    private ChessBoard board;

    public KnightMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public enum KnightPieceMove {
        ForwardLeft(-2,1),
        ForwardRight(-2,-1),
        DownLeft(2,1),
        DownRight(2,-1),
        ForwardSideLeft(1,-2),
        ForwardSideRight(1,2),
        BackSideLEFT(-1,-2),
        BackSideRIGHT(-1,2);

        private int rowChange;
        private int colChange;

        KnightPieceMove(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }
    }
    public List<ChessMove> getValidMoves() {
        List<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        for (KnightPieceMove move : KnightPieceMove.values()) {
            int newRow = myPosition.getRow() + move.rowChange;
            int newCol = myPosition.getColumn() + move.colChange;
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessMove newMove = new ChessMove(myPosition,newPosition,null);
            if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { // Bounds Check
                if(board.getPiece(newPosition) == null){
                    validMoves.add(newMove);
                } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(newMove);
                }
            }
        }
        return validMoves;
    }
}
