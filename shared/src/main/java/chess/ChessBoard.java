package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 *
 * Jake maybe let's create a "start game method" then with the reset game,
 * we can then kill the board and pieces and then just call that method
 */
public class ChessBoard {
    private ChessPiece[][] squares;//this is an 2D array of chess pieces
    public ChessBoard() {

        squares = new ChessPiece[8][8]; //8x8 chess board
    }

    public ChessBoard (ChessBoard boardDeepCopy){
        squares = Arrays.copyOf(boardDeepCopy.squares, boardDeepCopy.squares.length);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    private void boardWipe(){
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = null;
            }
        }
    }

    private void place_new_white_pawns(ChessPiece pawn) {
        for (int i = 0; i < 8; i++) {
            squares[1][i] = pawn;
        }
    }

    private void place_new_black_pawns(ChessPiece pawn) {
        for (int i = 0; i < 8; i++) {
            squares[6][i] = pawn;
        }
    }

    private void place_new_black_rook(ChessPiece rook) {
        squares[7][0] = rook;
        squares[7][7] = rook;
    }

    private void place_new_white_rook(ChessPiece rook) {
        squares[0][0] = rook;
        squares[0][7] = rook;
    }

    private void place_new_black_knight(ChessPiece knight) {
        squares[7][1] = knight;
        squares[7][6] = knight;
    }

    private void place_new_white_knight(ChessPiece knight) {
        squares[0][1] = knight;
        squares[0][6] = knight;
    }

    private void place_new_black_bishop(ChessPiece bishop) {
        squares[7][2] = bishop;
        squares[7][5] = bishop;
    }

    private void place_new_white_bishop(ChessPiece bishop) {
        squares[0][2] = bishop;
        squares[0][5] = bishop;
    }

    private void place_new_black_queen(ChessPiece queen) {
        squares[7][3] = queen;
    }

    private void place_new_white_queen(ChessPiece queen) {
        squares[0][3] = queen;
    }

    private void place_new_black_king(ChessPiece king) {
        squares[7][4] = king;
    }

    private void place_new_white_king(ChessPiece king) {
        squares[0][4] = king;
    }

    public void resetBoard() {
        boardWipe();
        ChessPiece BLACKPAWN = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece BLACKROOK = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece BLACKKNIGHT = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece BLACKBISHOP = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece BLACKQUEEN = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece BLACKKING = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        ChessPiece WHITEPAWN = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece WHITEROOK = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece WHITEKNIGHT = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece WHITEBISHOP = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece WHITEQUEEN = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece WHITEKING = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);

        place_new_black_pawns(BLACKPAWN);
        place_new_white_pawns(WHITEPAWN);

        place_new_black_rook(BLACKROOK);
        place_new_white_rook(WHITEROOK);

        place_new_black_knight(BLACKKNIGHT);
        place_new_white_knight(WHITEKNIGHT);

        place_new_black_bishop(BLACKBISHOP);
        place_new_white_bishop(WHITEBISHOP);

        place_new_black_king(BLACKKING);
        place_new_white_king(WHITEKING);

        place_new_black_queen(BLACKQUEEN);
        place_new_white_queen(WHITEQUEEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }



}
