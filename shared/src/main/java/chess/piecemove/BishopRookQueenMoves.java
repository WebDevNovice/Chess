package chess.piecemove;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

public class BishopRookQueenMoves {

    public BishopRookQueenMoves() {
    }

    public void makeLongDistancePieceMove(ChessBoard board, int[] move,
                                          ChessPosition myPosition, List<ChessMove> validMoves) {

        int newRow = myPosition.getRow() + move[0];
        int newCol = myPosition.getColumn() + move[1];
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessMove newMove = new ChessMove(myPosition, newPosition, null);
        do {
            if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) {
                if (board.getPiece(newPosition) == null) {
                    validMoves.add(newMove);
                    newRow += move[0];
                    newCol += move[1];
                    newPosition = new ChessPosition(newRow, newCol);
                    newMove = new ChessMove(myPosition, newPosition, null);
                } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(newMove);
                    break;
                } else {
                    break;
                }
            }
        } while ((newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9));
    }
}

