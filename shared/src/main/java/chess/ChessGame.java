package chess;

import java.util.Collection;

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
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (chessBoard.getPiece(startPosition) == null){
            return null;
        }
        return chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
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
            if (chessBoard.getPiece(move.getStartPosition()).getTeamColor().equals(getTeamTurn())){
                if (possibleMoves != null) {
                    for (ChessMove possibleMove : possibleMoves) {
                        if (possibleMove.equals(move)) {
                            chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition()));
                            chessBoard.addPiece(move.getStartPosition(), null);
                            if (chessBoard.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE) {
                                setTeamTurn(TeamColor.BLACK);
                            } else {
                                setTeamTurn(TeamColor.WHITE);
                            }
                        }
                        isInCheck(teamTurn);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                ChessPosition position = new ChessPosition(i, j);
                if (chessBoard.getPiece(position).getPieceType() != null){
                    Collection<ChessMove> possibleMoves = validMoves(position);
                    for (ChessMove possibleMove : possibleMoves) {
                        if (chessBoard.getPiece(possibleMove.getEndPosition()).getPieceType().equals(ChessPiece.PieceType.KING)//is the capture piece a king?
                            && chessBoard.getPiece(possibleMove.getStartPosition()).getTeamColor() != teamColor //is the piece capturing on the opposite team as the King?
                            && chessBoard.getPiece(possibleMove.getEndPosition()).getTeamColor() == teamColor) {
                                result = true;
                                kingInCheckPosition = possibleMove.getEndPosition();
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
