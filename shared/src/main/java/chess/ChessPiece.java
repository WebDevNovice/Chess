package chess;

import java.util.Collection;
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
     * we will probably need learn about if statements for Java
     *
     * @return Collection of valid moves
     */
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return pieceMoveCalc(board, myPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }

    private List<ChessMove> pieceMoveCalc(ChessBoard board, ChessPosition myPosition) {
        PieceType pieceType = getPieceType();
        switch (pieceType) {
            case KING:
                KingMoveCalc kingMoveCalc = new KingMoveCalc(board, myPosition);
                return kingMoveCalc.getValidMoves();
//            case QUEEN:
//                return (List<ChessPosition>) new QueenMoveCalc(board, myPosition);
//            case BISHOP:
//                return (List<ChessPosition>) new BishopMoveCalc(board, myPosition);
            case KNIGHT:
                KnightMoveCalc KnightMoveCalc = new KnightMoveCalc(board, myPosition);
                return KnightMoveCalc.getValidMoves();
//            case ROOK:
//                return (List<ChessPosition>) new RookMoveCalc(board, myPosition);
//            case PAWN:
//                return (List<ChessPosition>) new PawnMoveCalc(board, myPosition);
        }
        return java.util.List.of();
    }


}
