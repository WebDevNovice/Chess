package chess.piecemove;

import chess.*;

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
        WhiteForward(1,0),
        BlackForward(-1,0);

        private int rowChange;
        private int colChange;

        PawnPieceMove(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }
    }

    private void checkCaptureCaptures(ChessPosition targetPosition, List<ChessMove> validMoves){
        int newColRight = targetPosition.getColumn() + 1;
        int newColLeft = targetPosition.getColumn() - 1;
        int[] potentialMoves = {newColRight, newColLeft};
        for (int col : potentialMoves) {
            if (targetPosition.getRow() >= 1 && targetPosition.getRow() < 9 && col >= 1 && col < 9) {
                ChessPosition newPosition = new ChessPosition(targetPosition.getRow(), col);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        validMoves.add(newMove);
                    }
                }
            }
        }
    }

    private void checkInFront(ChessPosition newPosition, List<ChessMove> validMoves){
        ChessMove newMove = new ChessMove(myPosition,newPosition,null);
        if(board.getPiece(newPosition) == null){//is the target square empty?
            validMoves.add(newMove); // yes, well add it to the possible move list
            checkCaptureCaptures(newPosition, validMoves); //checks to see if we can capture on the right
        } else  {
            checkCaptureCaptures(newPosition, validMoves);
        }
    }

    private boolean checkStartingPosition(ChessPosition myPosition){
        return ((board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE) && myPosition.getRow() == 2) || //white's home row should be 2
                (board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.BLACK) && myPosition.getRow() == 7)); //black's home row should be 7
    }

    private void specialStartingMove(ChessPosition newPosition, List<ChessMove> validMoves){
        int newRow;
        int infrontRow;

        if(board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE ){
            newRow = newPosition.getRow() + 2;
            infrontRow = newPosition.getRow() + 1;
        }else{
            newRow = newPosition.getRow() - 2;
            infrontRow = newPosition.getRow() - 1;
        }

        int newCol = newPosition.getColumn();

        ChessPosition targetPosition = new ChessPosition(newRow, newCol);
        ChessPosition infrontPosition = new ChessPosition(infrontRow, newCol);
        ChessMove newMove = new ChessMove(myPosition,targetPosition,null);

        if(board.getPiece(infrontPosition) == null) {
            if(board.getPiece(targetPosition) == null){//is the target square empty?
                validMoves.add(newMove); // yes, well add it to the possible move list
            }
        }
    }

    private boolean promotionCheck(ChessPosition newPosition){
        return ((board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE) && newPosition.getRow() == 8) ||
                (board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.BLACK) && newPosition.getRow() == 1));
    }

    private void promotionMove(ChessPosition newPosition, List<ChessMove> validMoves){
        for (ChessPiece.PieceType type: ChessPiece.PieceType.values()){//for each type of promotion piece, do the following
            if(!type.equals(ChessPiece.PieceType.KING) && !type.equals(ChessPiece.PieceType.PAWN)){//Skips King and Pawn types
                if(board.getPiece(newPosition) == null){ //this checks the space right in front if it is empty
                    ChessMove newMove = new ChessMove(myPosition,newPosition,type); //this creates a new valid move
                    validMoves.add(newMove); //this adds it to our valid moves list
                }
                int[] captureMoves = {1,-1};
                for (int col : captureMoves) {
                    ChessPosition capturePosition = new ChessPosition(newPosition.getRow(), newPosition.getColumn()+col);//this change check for a capture
                    if(board.getPiece(capturePosition) != null){//this will check if the capture square is empty
                        if(board.getPiece(capturePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){//this will check if it is an oppenent piece
                            ChessMove newMove = new ChessMove(myPosition,capturePosition,type);//if yes, it will
                            validMoves.add(newMove);
                        }
                    }
                }
            }
        }
    }

    private void pawnMovement(PawnPieceMove move, List<ChessMove> validMoves){
        int newRow = myPosition.getRow() + move.rowChange; //this helps to check the target square
        int newCol = myPosition.getColumn() + move.colChange; //this helps to check the target square
        ChessPosition newPosition = new ChessPosition(newRow, newCol); //this helps to check the target square
        ChessMove newMove = new ChessMove(myPosition, newPosition, null); //this is creating a potential move to add to a list *NOTE* Still need to handle promotion

        if (newRow >= 1 && newRow < 9 && newCol >= 1 && newCol < 9) { // Bounds Check (COULD BE A SOURCE OF BAD CODE)

            if (checkStartingPosition(myPosition)) { //this is checking if I am in the starting col based on white or black pawns

                checkInFront(newPosition, validMoves);//this handles looks at everything in front of the pawn for potential movement or captures
                specialStartingMove(myPosition, validMoves); //this should let me move two spaces in front of me (if possible)


            } else if (promotionCheck(newPosition)){
                promotionMove(newPosition, validMoves);

            } else {
                checkInFront(newPosition, validMoves);
            }
        }
    }

    public List<ChessMove> getValidMoves() {
        List<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        if (board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            PawnPieceMove move = PawnPieceMove.WhiteForward;
            pawnMovement(move, validMoves);
        }else{
            PawnPieceMove move = PawnPieceMove.BlackForward;
            pawnMovement(move, validMoves);
        }
        return validMoves;
    }

    @Override
    public String toString() {
        return "PawnMoveCalc{" +
                "myPosition=" + myPosition +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnMoveCalc that = (PawnMoveCalc) o;
        return Objects.equals(myPosition, that.myPosition) && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myPosition, board);
    }

}