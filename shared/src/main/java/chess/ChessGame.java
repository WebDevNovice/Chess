package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessBoard;
    private TeamColor teamTurn;
    private ChessPosition kingInCheckPosition;

    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (teamTurn == null) {
            teamTurn = TeamColor.WHITE;
        }
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> badMoves = new ArrayList<>();

        if (chessBoard.getPiece(startPosition) == null){
            return null;
        }
        //I should implement to see if a move would result in a check for that color's team
        validMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        ChessPiece capturedPiece = null;
        ChessPiece promotedPawn = null;

        if (!validMoves.isEmpty()) {
            for (ChessMove move : validMoves) {
                if (move.getPromotionPiece() == null){ //check to make sure it's not a pawn promoting
                    if (chessBoard.getPiece(move.getEndPosition())==null){ //checking if final position is empty
                        chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition())); // if yes, we don't need to worry about keeping track of the capture piece
                    }
                    else {
                        capturedPiece = chessBoard.getPiece(move.getEndPosition()); // this saves the captured piece to be returned later
                        chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition())); // We then "capture" the old piece
                    }
                }
                else { //Pawn promotion route

                    if (chessBoard.getPiece(move.getEndPosition())==null){ //checking if the promotion spot is empty
                        promotedPawn = chessBoard.getPiece(move.getStartPosition());
                        ChessPiece promotionPiece = new ChessPiece(promotedPawn.getTeamColor(), move.getPromotionPiece());
                        chessBoard.addPiece(move.getEndPosition(), promotionPiece);
                    }
                    else {
                        promotedPawn = chessBoard.getPiece(move.getStartPosition());
                        capturedPiece = chessBoard.getPiece(move.getEndPosition());//this saves what piece was captured
                        ChessPiece promotionPiece = new ChessPiece(promotedPawn.getTeamColor(), move.getPromotionPiece());
                        chessBoard.addPiece(move.getEndPosition(), promotionPiece);
                    }
                }

                chessBoard.addPiece(move.getStartPosition(), null); //this leaves old location as blank

                if (isInCheck(chessBoard.getPiece(move.getEndPosition()).getTeamColor())) { //checking if when i moved, did I leave my King in check
                    badMoves.add(move); //if I did leave it in check, I add it to this list
                    if (move.getPromotionPiece() != null) {
                        chessBoard.addPiece(move.getStartPosition(), promotedPawn);//reset the board to how it was
                        chessBoard.addPiece(move.getEndPosition(), capturedPiece);//return the captured piece if there was one, if there wasn't, null is placed
                    }
                    else{
                        chessBoard.addPiece(move.getStartPosition(), chessBoard.getPiece(move.getEndPosition()));//reset the board to how it was
                        chessBoard.addPiece(move.getEndPosition(), capturedPiece);//return the captured piece if there was one, if there wasn't, null is placed
                    }
                    capturedPiece = null;
                }else{
                    if (move.getPromotionPiece() != null) {
                        chessBoard.addPiece(move.getStartPosition(), promotedPawn);//reset the board to how it was
                        chessBoard.addPiece(move.getEndPosition(), capturedPiece);//return the captured piece if there was one, if there wasn't, null is placed
                    }
                    else{
                        chessBoard.addPiece(move.getStartPosition(), chessBoard.getPiece(move.getEndPosition()));//reset the board to how it was
                        chessBoard.addPiece(move.getEndPosition(), capturedPiece);//return the captured piece if there was one, if there wasn't, null is placed
                    }
                    capturedPiece = null;
                }
            }
        }
            for (ChessMove move: badMoves){//this removes all the bad moves from our valid moves test
                validMoves.remove(move);
            }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        try {
            Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
            ChessPiece startPiece = chessBoard.getPiece(move.getStartPosition());
            if (startPiece != null && startPiece.getTeamColor() == teamTurn) {
                    if (!possibleMoves.isEmpty()) {
                        for (ChessMove possibleMove : possibleMoves) {
                            if (possibleMove.equals(move)) {
                                if (move.getPromotionPiece() == null){
                                chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition()));
                                }
                                else {
                                    ChessPiece promotionPiece = new ChessPiece(teamTurn, move.getPromotionPiece());
                                    chessBoard.addPiece(move.getEndPosition(), promotionPiece);
                                }

                                chessBoard.addPiece(move.getStartPosition(), null);

                                if (getTeamTurn() == TeamColor.WHITE) {
                                    setTeamTurn(TeamColor.BLACK);
                                    break;
                                } else {
                                    setTeamTurn(TeamColor.WHITE);
                                    break;
                                }
                            }
                            //                        isInCheck(teamTurn);
                        }
                    }
                }else {
                throw new InvalidMoveException();
            }
        } catch (Exception e) {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean result = false;
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){ //this is going over ever square on the board looking for pieces
                ChessPosition position = new ChessPosition(i, j);
                if (chessBoard.getPiece(position) == null) {
                    continue;
                }
                else{
                    Collection<ChessMove> possibleMoves = chessBoard.getPiece(position).pieceMoves(chessBoard, position);
                        for (ChessMove possibleMove : possibleMoves) {
                            if (chessBoard.getPiece(possibleMove.getEndPosition())!=null){
                                if (chessBoard.getPiece(possibleMove.getEndPosition()).getPieceType().equals(ChessPiece.PieceType.KING)//is the capture piece a king?
                                        && chessBoard.getPiece(possibleMove.getStartPosition()).getTeamColor() != teamColor //is the piece capturing on the opposite team as the King?
                                        && chessBoard.getPiece(possibleMove.getEndPosition()).getTeamColor() == teamColor) {
                                    result = true;
                                    kingInCheckPosition = possibleMove.getEndPosition();
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
        return result;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean result = true;
        if (isInCheck(teamColor)) {
            Collection<ChessMove> possibleMoves = validMoves(kingInCheckPosition);
            for (ChessMove possibleMove : possibleMoves) {
                ChessBoard newBoard = chessBoard;
                if(isInCheck(teamColor)){
                    result = false;
                }
            }
        }
        return result;
//            for (int i = 0; i < 8; i++){
//                for (int j = 0; j < 8; j++){
//                    ChessPosition position = new ChessPosition(i, j);
//                    if (chessBoard.getPiece(position).getPieceType() != null){
//                        if (chessBoard.getPiece(position).getPieceType().equals(ChessPiece.PieceType.KING)) {
//                            Collection<ChessMove> possibleMoves = validMoves(position);
//                            for (ChessMove possibleMove : possibleMoves) {
//                                if ()
//                            }
//                        }
//                        }
//                    }
//                    }
//        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        if (chessBoard == null) {
            chessBoard = new ChessBoard();
            chessBoard.resetBoard();
        }
        return chessBoard;
    }


}
