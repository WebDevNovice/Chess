package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardCreator {
    public static ChessGame chessGame;
    public static ChessBoard chessBoard;
    public static ChessGame.TeamColor teamColor;

    public BoardCreator(ChessGame chessGame, ChessGame.TeamColor teamColor) {
        this.chessGame = chessGame;
        this.chessBoard = chessGame.getBoard();
        this.teamColor = teamColor;
    }

    public static void main() {
        drawChessBoard();
    }

    public static void drawChessBoard() {
        String darkGreenSquare = SET_BG_COLOR_DARK_GREEN + EMPTY + RESET_BG_COLOR;
        String magentaSquare = SET_BG_COLOR_MAGENTA + EMPTY + RESET_BG_COLOR;

        String[] bgColors ={darkGreenSquare, magentaSquare};

        StringBuilder chessBoardBuilder = new StringBuilder();
        String[] header = {"   ", " A ", " B ", " C ", " D ", " E ", " F ", " G ", " H  \n"};
        String[] vertNumber = {"  ", " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};

        drawHeaders(header, chessBoardBuilder);

        if (teamColor == ChessGame.TeamColor.BLACK) {
            for (int row = 1; row < 9; row++) {
                doColLoop(row,chessBoardBuilder,vertNumber, bgColors);
                chessBoardBuilder.append("\n");
            }
        } else {
            for (int row = 8; row > 0; row--) {
                doColLoop(row,chessBoardBuilder,vertNumber, bgColors);
                chessBoardBuilder.append("\n");
            }
        }
        System.out.println(chessBoardBuilder.toString());
    }

    public static void doColLoop(int row, StringBuilder chessBoardBuilder, String[] vertNumber, String[] bgColor) {
        for (int col = 0; col < 9; col++) {
            if ((row + col) % 2 == 0) {
                drawSquare(bgColor[0], chessBoardBuilder, col, row, vertNumber);
            } else if ((row + col) % 2 == 1) {
                drawSquare(bgColor[1], chessBoardBuilder, col, row, vertNumber);
            }
        }
    }

    private static String setTextLightGray(String text) {
        return SET_TEXT_BOLD + SET_TEXT_COLOR_LIGHT_GREY + text + RESET_TEXT_COLOR;
    }

    private static void drawHeaders(String[] header, StringBuilder chessStringBuilder) {
        for (String s : header) {
            chessStringBuilder.append(setTextLightGray(s));
        }
    }

    private static void drawVertNumbers(String[] vertNumber, StringBuilder chessStringBuilder, int index) {
        chessStringBuilder.append(setTextLightGray(vertNumber[index]));
    }

    public static void drawSquare(String squareColor, StringBuilder chessBoardBuilder, int col, int row, String[] vertNumber) {
        if (col == 0) {
            drawVertNumbers(vertNumber, chessBoardBuilder, row);
        } else {
            getPieceInformation(row, col, chessBoardBuilder, squareColor);
        }
    }

    public static void getPieceInformation(int row, int col, StringBuilder chessBoardBuilder, String bgColor) {
        ChessPosition currentPosition = new ChessPosition(row, col);
        ChessPiece piece = chessBoard.getPiece(currentPosition);
        if (piece == null) {
            chessBoardBuilder.append(bgColor);
            return;
        }
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        switch (piece.getPieceType()) {
            case KING -> chessBoardBuilder.append(drawChessPiece(" K ", pieceColor, bgColor));
            case QUEEN -> chessBoardBuilder.append(drawChessPiece(" Q ", pieceColor, bgColor));
            case BISHOP -> chessBoardBuilder.append(drawChessPiece(" B ", pieceColor, bgColor));
            case KNIGHT -> chessBoardBuilder.append(drawChessPiece(" N ", pieceColor, bgColor));
            case ROOK -> chessBoardBuilder.append(drawChessPiece(" R ", pieceColor, bgColor));
            case PAWN -> chessBoardBuilder.append(drawChessPiece(" P ", pieceColor, bgColor));
        }
    }

    public static String drawChessPiece(String piece, ChessGame.TeamColor teamColor, String bgColor) {
        if (teamColor == ChessGame.TeamColor.BLACK) {
            return SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + bgColor.replace(EMPTY, piece) + RESET_TEXT_COLOR + RESET_BG_COLOR;
        } else {
            return SET_TEXT_BOLD + SET_TEXT_COLOR_WHITE + bgColor.replace(EMPTY, piece) + RESET_TEXT_COLOR + RESET_BG_COLOR;
        }
    }
}
