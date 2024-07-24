package chess.piecemove;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class KingKnightMoves {
    public KingKnightMoves() {
    }

    public void shortDistanceMove(ChessBoard board, int[] move,
                                  ChessPosition myPosition, List<ChessMove> validMoves){
        int newRow = myPosition.getRow() + move[0];
        int newCol = myPosition.getColumn() + move[1];
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessMove newMove = new ChessMove(myPosition, newPosition, null);
        if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { // Bounds Check
            if(board.getPiece(newPosition) == null){
                validMoves.add(newMove);
            } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validMoves.add(newMove);
            }
        }
    }
}
