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
    private int[][] kingMoves = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1},
            {1, 1},
            {-1, -1},
            {1, -1},
            {-1, 1}
    };

    public KingMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public List<ChessMove> getValidMoves() {
        List<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        for (int[] move : kingMoves) {
            KingKnightMoves kingKnightMoves = new KingKnightMoves();
            kingKnightMoves.shortDistanceMove(board, move, myPosition, validMoves);
        }
        return validMoves;
    }
}
