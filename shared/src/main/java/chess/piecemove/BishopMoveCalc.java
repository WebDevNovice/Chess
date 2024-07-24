package chess.piecemove;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * So this needs to store and array of positions
 * that means that I'll need to be able to "visit" each step
 * and check to see if it is valid,
 * if it is valid, append it to my list
 * if not skip
 */
public class BishopMoveCalc {

    private ChessPosition myPosition;
    private ChessBoard board;
    private int[][] bishopMoves = {
            {1, 1},
            {1, -1},
            {-1, 1},
            {-1, -1}
    };


    public BishopMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }


    public List<ChessMove> getValidMoves() {
        List<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        for (int[] move: bishopMoves) {
            BishopRookQueenMoves bishopRookQueenMoves = new BishopRookQueenMoves();
            bishopRookQueenMoves.makeLongDistancePieceMove(board, move, myPosition, validMoves);
        }
        return validMoves;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BishopMoveCalc that = (BishopMoveCalc) o;
        return Objects.equals(myPosition, that.myPosition) && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myPosition, board);
    }
}
