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
public class PawnMoveCalc {

    private ChessPosition myPosition;
    private ChessBoard board;

    public PawnMoveCalc(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public enum PawnPieceMove {
        UP(-1,0);

        private int rowChange;
        private int colChange;

        PawnPieceMove(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }
    }

    private void check_capture_right(ChessPosition target_position, List<ChessMove> ValidMoves){
        int newCol = target_position.getColumn() + 1; //shift the col right
        ChessPosition newPosition = new ChessPosition(target_position.getRow(), newCol);
        ChessMove newMove = new ChessMove(myPosition,newPosition,null);
        if(board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ValidMoves.add(newMove);
        }
    }

    private void check_capture_left(ChessPosition target_position, List<ChessMove> ValidMoves){
        int newCol = target_position.getColumn() - 1; //shift the col left
        ChessPosition newPosition = new ChessPosition(target_position.getRow(), newCol);
        ChessMove newMove = new ChessMove(myPosition,newPosition,null);
        if(board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ValidMoves.add(newMove);
        }
    }

    private void not_starting_position(ChessPosition newPosition, List<ChessMove> ValidMoves){
        ChessMove newMove = new ChessMove(myPosition,newPosition,null);
        if(board.getPiece(newPosition) == null){//is the target square empty?
            ValidMoves.add(newMove); // yes, well add it to the possible move list
            check_capture_right(newPosition, ValidMoves); //checks to see if we can capture on the right
            check_capture_left(newPosition, ValidMoves); //checks to see if we can capture on the left
        } else  {
            check_capture_left(newPosition, ValidMoves);
            check_capture_right(newPosition, ValidMoves);
        }
    }

    public List<ChessMove> getValidMoves() {
        List<ChessMove> ValidMoves;
        ValidMoves = new ArrayList<>();

        for (PawnPieceMove move : PawnPieceMove.values()) {
            int newRow = myPosition.getRow() + move.rowChange; //this helps to check the target square
            int newCol = myPosition.getColumn() + move.colChange; //this helps to check the target square
            ChessPosition newPosition = new ChessPosition(newRow, newCol); //this helps to check the target square
            ChessMove newMove = new ChessMove(myPosition,newPosition,null); //this is creating a potential move to add to a list *NOTE* Still need to handle promotion
            if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { // Bounds Check
                //here we should check if is it the starting position or not
                not_starting_position(newPosition, ValidMoves); //this handles the case where it is not the starting case (might be able to rename and reuse it somewhere down the line)
            }
        }
        return ValidMoves;
    }
}
