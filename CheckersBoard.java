import java.awt.*;
import javax.swing.*;

public class CheckersBoard extends JFrame {

	// dimensions of each square
	public static final int SQUARE_WIDTH = 50;
	public static final int SQUARE_HEIGHT = 50;

	// size of board
	public static final int BOARD_WIDTH = 8;
	public static final int BOARD_HEIGHT = 8;

	// size of window
	public static final int WINDOW_WIDTH = BOARD_WIDTH * SQUARE_WIDTH;
	public static final int WINDOW_HEIGHT = BOARD_HEIGHT * SQUARE_HEIGHT;
	
	// board dimensions
	private int boardWidth;
	private int boardHeight;
	
	// 2d array to hold pieces
	private Piece[][] board;

	// state variables
	private boolean gameOver;
	private boolean inCheck;
	private boolean blackTurn; // true = black turn, false = white turn
	private Piece pieceToMove;
	private Point moveFrom;
	private Point moveTo;
	private boolean multipleInARow;

	// strings
	private String gameStatus;
	private String winner;


	public CheckersBoard() {
		// set window properties
		super("Checkers");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		// set board dimensions
		boardWidth = BOARD_WIDTH;
		boardHeight = BOARD_HEIGHT;

		// set game state
		gameOver = false;
		inCheck = false;
		blackTurn = true;
		pieceToMove = null;
		moveFrom = null;
		moveTo = null;
		multipleInARow = false;

		// set strings
		gameStatus = "";
		winner = "";

		// initialise board
		board = new Piece[boardWidth][boardHeight];
		initBoard();
	}

	private void initBoard() {
		// Initialise the board with the pieces
		// in their starting positions

		// initialise black pieces
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < boardWidth; col++) {
				// check if position should have a piece
				if (isBlackStartingPosition(row, col)) {
					board[row][col] = new Piece(true);
				}
			}
		}

		// initialise white pieces
		for (int row = boardHeight - 1; row > boardHeight - 4; row--) {
			for (int col = 0; col < boardWidth; col++) {
				// check if position should have a piece
				if (isWhiteStartingPosition(row, col)) {
					board[row][col] = new Piece(false);
				}
			}
		}
	}

	private boolean isBlackStartingPosition(int row, int col) {
		// checks if a position is a black starting position
		// (top left, top centre and top right of the board)

		if ((row == 0 && (col == 0 || col == 3 || col == 6)) ||
			(row == 1 && (col == 1 || col == 3 || col == 5)) ||
			(row == 2 && (col == 2 || col == 3 || col == 4))) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isWhiteStartingPosition(int row, int col) {
		// checks if a position is a white starting position
		// (bottom left, bottom centre and bottom right of the board)

		if ((row == 5 && (col == 0 || col == 3 || col == 6)) ||
			(row == 6 && (col == 1 || col == 3 || col == 5)) ||
			(row == 7 && (col == 2 || col == 3 || col == 4))) {
			return true;
		} else {
			return false;
		}
	}

	public void paint(Graphics g) {
		// Draw board
		for (int row = 0; row < boardHeight; row++) {
			for (int col = 0; col < boardWidth; col++) {
				int x = col * SQUARE_WIDTH;
				int y = row * SQUARE_HEIGHT;
				// check if square has a piece
				if (board[row][col] != null) {
					// draw piece
					board[row][col].draw(g, x, y);
				} else {
					// draw empty square
					g.setColor(Color.BLACK);
					g.fillRect(x + 2, y + 2, SQUARE_WIDTH - 4, SQUARE_HEIGHT - 4);
				}
			}
		}

		// draw game message
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString(gameStatus, 10, WINDOW_HEIGHT - 10);
	}

	public void selectPiece(int x, int y) {
		// get row and col of clicked square
		int row = y / SQUARE_HEIGHT;
		int col = x / SQUARE_WIDTH;
		
		// check if a piece is present at row and col
		pieceToMove = board[row][col];
		
		// get moveFrom
		if (pieceToMove != null) {
			moveFrom = new Point(col, row);
		}
	}

	public void movePiece(int x, int y) {		
		// get row and col of clicked square		
		int row = y / SQUARE_HEIGHT;
		int col = x / SQUARE_WIDTH;

		// check if valid move
		if (isValidMove(col, row)) {
			// move piece
			board[moveTo.y][moveTo.x] = pieceToMove;
			board[moveFrom.y][moveFrom.x] = null;

			// check if a capture move was performed
			if (isCaptureMove(col, row)) {
				// remove captured piece
				board[row][col] = null;
			}

			// check if the piece was promoted to a King
			if (isKingPromotion(col, row)) {
				// promote piece to King
				board[row][col].isKing = true;
			}

			// check if a multi-capture move is possible
			if (isMultiCaptureMove(col, row)) {
				multipleInARow = true;
				moveFrom.x = col;
				moveFrom.y = row;
			} else {
				// reset state variables
				pieceToMove = null;
				moveFrom = null;
				moveTo = null;
				multipleInARow = false;
		
				// check if game is over
				if (isGameOver()) {
					endGame();
				} else {
					// switch turns
					blackTurn = !blackTurn;
				}
			}
		}
	}

	private boolean isValidMove(int col, int row) {
		// checks if a move is valid according to the classic rules of checkers

		// check if there is a piece to move
		if (pieceToMove == null) {
			gameStatus = "Please select a piece to move.";
			return false;
		}

		// check if the move is within the board
		if (!isWithinBoard(col, row)) {
			gameStatus = "Can't move piece out of the board!";
			return false;
		}

		// check if the move is one square diagonally
		if (!isOneSquareDiagonally(col, row)) {
			gameStatus = "Piece can only move one square diagonally!";
			return false;
		}

		// check if the move is forward
		if (!isForwardMove(col, row)) {
			gameStatus = "Piece can only move forward!";
			return false;
		}

		// check if the move is to an empty square
		if (!isEmptySquare(col, row)) {
			gameStatus = "Can't move to a non-empty square!";
			return false;
		}

		// check if the move is a capture move
		if (isCaptureMove(col, row)) {
			// check if the piece is a King
			if (pieceToMove.isKing) {
				// check if the move is forward and backward
				if (!isForwardAndBackwardCaptureMove(col, row)) {
					gameStatus = "King can only capture forward and backward!";
					return false;
				}
			}
		}
		
		// set moveTo
		moveTo = new Point(col, row);

		return true;
	}

	private boolean isWithinBoard(int col, int row) {
		// checks if a move is within the board

		if (col < 0 || col > boardWidth - 1 || row < 0 || row > boardHeight - 1) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isOneSquareDiagonally(int col, int row) {
		// checks if a move is one square diagonally

		if ((moveFrom.x == col - 1 && moveFrom.y == row - 1) ||
			(moveFrom.x == col + 1 && moveFrom.y == row - 1) ||
			(moveFrom.x == col - 1 && moveFrom.y == row + 1) ||
			(moveFrom.x == col + 1 && moveFrom.y == row + 1)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isForwardMove(int col, int row) {
		// checks if a move is forward (for black pieces)

		if (pieceToMove.isBlack && row < moveFrom.y) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmptySquare(int col, int row) {
		// checks if a move is to an empty square

		if (board[row][col] == null) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isCaptureMove(int col, int row) {
		// checks if a move is a capture move

		// check if a piece is present between moveFrom and moveTo
		int midCol = (moveFrom.x + col) / 2;
		int midRow = (moveFrom.y + row) / 2;
		if (board[midRow][midCol] != null) {
			// check if the piece to capture is the opponent piece 
			if (pieceToMove.isBlack != board[midRow][midCol].isBlack) {
				return true;
			}
		}

		return false;
	}

	private boolean isKingPromotion(int col, int row) {
		// checks if a piece was promoted to a King

		if (pieceToMove.isBlack && row == 0) {
			return true;
		} else if (!pieceToMove.isBlack && row == boardHeight - 1) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isMultiCaptureMove(int col, int row) {
		// checks if a multi-capture move is possible

		// check if a multi-capture move has been done before
		if (multipleInARow) {
			// check if there is a piece to capture
			int midCol = (moveFrom.x + col) / 2;
			int midRow = (moveFrom.y + row) / 2;
			if (board[midRow][midCol] != null) {
				// check if the piece to capture is the opponent piece 
				if (pieceToMove.isBlack != board[midRow][midCol].isBlack) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isForwardAndBackwardCaptureMove(int col, int row) {
		// checks if a King's move is forward and backward

		if ((moveFrom.x == col - 2 && moveFrom.y == row - 2) ||
			(moveFrom.x == col + 2 && moveFrom.y == row - 2) ||
			(moveFrom.x == col - 2 && moveFrom.y == row + 2) ||
			(moveFrom.x == col + 2 && moveFrom.y == row + 2)) {
			return true;
		} else {
			return false;
		}
	}

	private void endGame() {
		// set game over state
		gameOver = true;

		// check if game resulted in a draw
		if (inCheck) {
			// check who's turn it is
			if (blackTurn) {
				winner = "White";
			} else {
				winner = "Black";
			}
			gameStatus = "Checkmate! " + winner + " won!";
		} else {
			gameStatus = "It's
