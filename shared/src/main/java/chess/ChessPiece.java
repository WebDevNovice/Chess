package chess;

import chess.pieceMove.*;

import java.util.List;
import java.util.Objects;

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
     * <p>
     *
     *
     * @return Collection of valid moves
     */
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return pieceMoveCalc(board, myPosition);
    }

    private List<ChessMove> pieceMoveCalc(ChessBoard board, ChessPosition myPosition) {
        PieceType pieceType = getPieceType();
        switch (pieceType) {
            case KING:
                KingMoveCalc kingMoveCalc = new KingMoveCalc(board, myPosition);
                return kingMoveCalc.getValidMoves();
            case QUEEN:
                QueenMoveCalc queenMoveCalc = new QueenMoveCalc(board, myPosition);
                return queenMoveCalc.getValidMoves();
            case BISHOP:
                BishopMoveCalc bishopMoveCalc = new BishopMoveCalc(board, myPosition);
                return bishopMoveCalc.getValidMoves();
            case KNIGHT:
                KnightMoveCalc KnightMoveCalc = new KnightMoveCalc(board, myPosition);
                return KnightMoveCalc.getValidMoves();
            case ROOK:
                RookMoveCalc rookMoveCalc = new RookMoveCalc(board, myPosition);
                return rookMoveCalc.getValidMoves();
            case PAWN:
                PawnMoveCalc pawnMoveCalc = new PawnMoveCalc(board, myPosition);
                return pawnMoveCalc.getValidMoves();
        }
        return java.util.List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return teamColor == piece.teamColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", type=" + type +
                '}';
    }
}
