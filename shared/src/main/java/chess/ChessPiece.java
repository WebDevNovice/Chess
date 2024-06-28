package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
//    This is the constructor

    private ChessGame.TeamColor teamColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     * Jake - Look into enum for java
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * we will probably need learn about if statements for Java
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return pieceMoveCalc(board, myPosition);
    }
    private Collection<ChessMove> pieceMoveCalc(ChessBoard board, ChessPosition myPosition) {
        PieceType pieceType = getPieceType();
        switch (pieceType) {
            case KING:
//                I want to feed the KingMoveCalc here
                return (Collection<ChessMove>) new KingMoveCalc(board, myPosition);
            case QUEEN:
                return (Collection<ChessMove>) new QueenMoveCalc(board, myPosition);
            case BISHOP:
                return (Collection<ChessMove>) new BishopMoveCalc(board, myPosition);
            case KNIGHT:
                return (Collection<ChessMove>) new KnightMoveCalc(board, myPosition);
            case ROOK:
                return (Collection<ChessMove>) new RookMoveCalc(board, myPosition);
            case PAWN:
                return (Collection<ChessMove>) new PawnMoveCalc(board, myPosition);
        }
        return java.util.List.of();
    }


}
