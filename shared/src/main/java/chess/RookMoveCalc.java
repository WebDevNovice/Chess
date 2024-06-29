package chess;

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

    public RookMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public enum RookPieceMove {
        UP(-1,0),
        Down(1,0),
        LEFT(0,-1),
        RIGHT(0,1);

        private int rowChange;
        private int colChange;

        RookPieceMove(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }
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
        List<ChessMove> ValidMoves;
        ValidMoves = new ArrayList<>();

        for (RookPieceMove move : RookPieceMove.values()) {//this will iterate through all the potential moves
            int newRow = myPosition.getRow() + move.rowChange; //this creates the new row value
            int newCol = myPosition.getColumn() + move.colChange; //this creates the new col value
            ChessPosition newPosition = new ChessPosition(newRow, newCol); //this just create an instance of a position object
            ChessMove newMove = new ChessMove(myPosition, newPosition, null); //similar to the previous line, just for a move object
            do { // my thinking here is that I wanted a way to be able to show that a piece could move multiple spaces
                if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { //this is just an initial check that it is in bounds (I kinda double-check this)
                    if (board.getPiece(newPosition) == null) { //is the new position empty?
                        ValidMoves.add(newMove); //if yes, lets add it to our potential move list
                        newRow += move.rowChange; //let's update our new row and col
                        newCol += move.colChange;
                        newPosition = new ChessPosition(newRow, newCol);//since we have new row and col, we need a new position object
                        newMove = new ChessMove(myPosition, newPosition, null);// and a new move object
                    } else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //simple check to see if they are the same team or not
                        ValidMoves.add(newMove);//if they are not, we can add it a potential capture
                        break;//if we capture a piece, we are not going to be moving beyond it now
                    } else {
                        break;//If we get to this case, that means that we have arrived at the friendly piece, and we cannot move in this direction anymore
                    }
                }
            } while ((newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9)); //I believe this is checking the new row & col (see lines 66 & 67) to see if they are in bounds or not
        }
        return ValidMoves;
    }
}
