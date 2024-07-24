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
    private int[][] knightMoves ={
            {-2,1},
            {-2,-1},
            {2,1},
            {2,-1},
            {-1,2},
            {-1,-2},
            {1,-2},
            {1,2}
    };

    public KnightMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }


    public List<ChessMove> getValidMoves() {
        List<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        for (int[] move : knightMoves) {
            KingKnightMoves kingKnightMoves = new KingKnightMoves();
            kingKnightMoves.shortDistanceMove(board, move, myPosition, validMoves);
        }
        return validMoves;
    }
}
