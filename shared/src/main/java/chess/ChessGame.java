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
    private ChessBoard chessBoard = new ChessBoard();
    private TeamColor teamTurn;
    private ChessPosition kingInCheckPosition;
    private boolean gameOver;

    public ChessGame() {
        chessBoard.resetBoard();
        getTeamTurn();
    }

    public boolean resignGame(){
        gameOver = true;
        return gameOver;
    }

    public boolean isGameOver(TeamColor teamColor) {
        if (gameOver) {
            return true;
        }

        if (teamTurn == teamColor) {
            if (isInCheckmate(teamColor)) {
                gameOver = true;
                return true;
            }
            if (isInStalemate(teamColor)) {
                gameOver = true;
                return true;
            }
        }
        return false;
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

    private ArrayList<ChessPiece> handleValidMovesNotEmpty(ChessMove move, ChessPiece capturedPiece, ChessPiece promotedPawn) {
        ArrayList<ChessPiece> pieceCollection = new ArrayList<>();
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
        pieceCollection.add(capturedPiece);
        pieceCollection.add(promotedPawn);
        return pieceCollection;
    }

    private void resetBoard (ChessPiece promotedPawn, ChessPiece capturedPiece, ChessMove move) {
        if (move.getPromotionPiece() != null) {
            chessBoard.addPiece(move.getStartPosition(), promotedPawn);//reset the board to how it was
            chessBoard.addPiece(move.getEndPosition(), capturedPiece);//return the captured piece if there was one, if there wasn't, null is placed
        } else {
            chessBoard.addPiece(move.getStartPosition(), chessBoard.getPiece(move.getEndPosition()));//reset the board to how it was
            chessBoard.addPiece(move.getEndPosition(), capturedPiece);//return the captured piece if there was one, if there wasn't, null is placed
        }
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
            return validMoves;
        }
        //I should implement to see if a move would result in a check for that color's own team
        validMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        ChessPiece capturedPiece = null;
        ChessPiece promotedPawn = null;

        if (!validMoves.isEmpty()) {

            for (ChessMove move : validMoves) {
                ArrayList<ChessPiece> pieceCollection;
                pieceCollection = handleValidMovesNotEmpty(move, capturedPiece, promotedPawn);
                capturedPiece = pieceCollection.get(0);
                promotedPawn = pieceCollection.get(1);

                chessBoard.addPiece(move.getStartPosition(), null); //this leaves old location as blank

                if (isInCheck(chessBoard.getPiece(move.getEndPosition()).getTeamColor())) { //checking if when i moved, did I leave my King in check
                    badMoves.add(move); //if I did leave it in check, I add it to this list
                    resetBoard(promotedPawn, capturedPiece, move);
                } else {
                    resetBoard(promotedPawn, capturedPiece, move);
                }
                capturedPiece = null;
            }

            for (ChessMove move : badMoves) {//this removes all the bad moves from our valid moves test
                validMoves.remove(move);
            }
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
            if (isInCheck(chessBoard.getPiece(move.getStartPosition()).getTeamColor())){
                throw new InvalidMoveException();
            }
            Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
            if (possibleMoves.isEmpty()) {
                throw new InvalidMoveException();
            }
            ChessPiece startPiece = chessBoard.getPiece(move.getStartPosition());
            if (startPiece != null && startPiece.getTeamColor() == teamTurn) {
                if (!possibleMoves.isEmpty()) {
                    if (!possibleMoves.contains(move)) {
                        throw new InvalidMoveException();
                    }
                    for (ChessMove possibleMove : possibleMoves) {
                        makeLegitMove(move, possibleMove);
                    }
                }
            }else {
                throw new InvalidMoveException();
            }
        } catch (Exception e) {
            throw new InvalidMoveException();
        }
    }

    private void makeLegitMove(ChessMove move, ChessMove possibleMove) {
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
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
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
            for (int j = 1; j <= 8; j++) { //this is going over ever square on the board looking for pieces
                ChessPosition position = new ChessPosition(i, j);
                if (chessBoard.getPiece(position) == null) {
                    continue;
                } else {
                    result = isKingInCheck(position, teamColor, result);
                    if (result) {
                        return result;
                    }
                }
            }
        }
        return result;
    }

    private boolean isKingInCheck(ChessPosition position,TeamColor teamColor, boolean result) {
    Collection<ChessMove> possibleMoves = chessBoard.getPiece(position).pieceMoves(chessBoard, position);
    for (ChessMove possibleMove : possibleMoves) {
        if (chessBoard.getPiece(possibleMove.getEndPosition()) != null) {
            if (chessBoard.getPiece(possibleMove.getEndPosition()).getPieceType().equals(ChessPiece.PieceType.KING)
                    && chessBoard.getPiece(possibleMove.getStartPosition()).getTeamColor() != teamColor
                    && chessBoard.getPiece(possibleMove.getEndPosition()).getTeamColor() == teamColor) {
                result = true;
                kingInCheckPosition = possibleMove.getEndPosition();
                return result;
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
        if (!isInCheck(teamColor)) {
            return false;
        }
        else {
            if (isKingInCheckmate(teamColor)){
                gameOver = true;
                return true;
            }
            return false;
        }
    }

    private Boolean isKingInCheckmate(TeamColor teamColor) {

        Collection<ChessMove> possibleMoves = validMoves(kingInCheckPosition);
        if (!possibleMoves.isEmpty()) {
            return false;
        }

        for (int i =1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (chessBoard.getPiece(position) == null) {
                    continue;
                }
                if (chessBoard.getPiece(position).getTeamColor() == teamColor) {
                    Collection<ChessMove> allyPotentialMoves;
                    allyPotentialMoves = validMoves(position);
                    if (!allyPotentialMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheckmate(teamColor)) {
            return false;
        }
        for (int i =1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (chessBoard.getPiece(position) == null) {
                    continue;
                }
                else{
                    if (chessBoard.getPiece(position).getTeamColor() == teamColor) {
                        Collection<ChessMove> teamColorPotentialMoves = new ArrayList<>();
                        teamColorPotentialMoves = validMoves(position);
                        if (!teamColorPotentialMoves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        gameOver = true;
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
            chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        if (chessBoard == null) {
            return null;
        }
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return gameOver == chessGame.gameOver && Objects.equals(chessBoard, chessGame.chessBoard) &&
                teamTurn == chessGame.teamTurn && Objects.equals(kingInCheckPosition, chessGame.kingInCheckPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, teamTurn, kingInCheckPosition, gameOver);
    }
}
