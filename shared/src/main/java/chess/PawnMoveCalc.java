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

    private void check_in_front(ChessPosition newPosition, List<ChessMove> ValidMoves){
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

    private boolean check_starting_position(ChessPosition myPosition){
        return ((board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE) && myPosition.getRow() == 1) ||
                (board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.BLACK) && myPosition.getRow() == 6));
    }

    private void special_starting_move(ChessPosition newPosition, List<ChessMove> ValidMoves){
        int newCol = newPosition.getColumn() + 1;
        ChessPosition targetPosition = new ChessPosition(newPosition.getRow(), newCol);
        ChessMove newMove = new ChessMove(myPosition,targetPosition,null);
        if(board.getPiece(targetPosition) == null){//is the target square empty?
            ValidMoves.add(newMove); // yes, well add it to the possible move list
        }
    }

    private boolean promotion_check(ChessPosition newPosition){
        return ((board.getPiece(newPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE) && newPosition.getRow() == 7) ||
                (board.getPiece(newPosition).getTeamColor().equals(ChessGame.TeamColor.BLACK) && newPosition.getRow() == 0));
    }

    public List<ChessMove> getValidMoves() {
        List<ChessMove> ValidMoves;
        ValidMoves = new ArrayList<>();

        for (PawnPieceMove move : PawnPieceMove.values()) {

            int newRow = myPosition.getRow() + move.rowChange; //this helps to check the target square
            int newCol = myPosition.getColumn() + move.colChange; //this helps to check the target square
            ChessPosition newPosition = new ChessPosition(newRow, newCol); //this helps to check the target square
            ChessMove newMove = new ChessMove(myPosition,newPosition,null); //this is creating a potential move to add to a list *NOTE* Still need to handle promotion

            if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { // Bounds Check (COULD BE A SOURCE OF BAD CODE)

                if (check_starting_position(myPosition)){

                    check_in_front(newPosition, ValidMoves); //this handles looks at everything in front of the pawn for potential movement or captures
                    special_starting_move(myPosition, ValidMoves);

                }else if (promotion_check(newPosition)){

                }
                else {
                    check_capture_left(newPosition, ValidMoves);
                }
            }
        }
        return ValidMoves;
    }
}
