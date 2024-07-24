package chess.piecemove;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * So this needs to store and array of positions
 * that means that I'll need to be able to "visit" each step
 * and check to see if it is valid,
 * if it is valid, append it to my list
 * if not skip
 */
public class RookMoveCalc {

    private ChessPosition myPosition;
    private ChessBoard board;
    int[][] rookMoves = {
            {1, 0},
            {0, -1},
            {0, 1},
            {-1, -0}
    };

    public RookMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RookMoveCalc that = (RookMoveCalc) o;
        return Objects.equals(myPosition, that.myPosition) && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myPosition, board);
    }

    public List<ChessMove> getValidMoves() {
        List<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        for (int[] move : rookMoves) {//this will iterate through all the potential moves
            BishopRookQueenMoves bishopRookQueenMoves = new BishopRookQueenMoves();
            bishopRookQueenMoves.makeLongDistancePieceMove(board, move, myPosition, validMoves);
        }
        return validMoves;
    }
}
