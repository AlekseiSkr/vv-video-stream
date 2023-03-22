public class Checkers {
    private Piece[][] board;
    private boolean turn; //true for white, false for black
    private boolean gameOver;
    private int movesDone;

    public Checkers() {
        board = new Piece[8][8];
        turn = true;
        gameOver = false;
        movesDone = 0;
        initBoard();
    }

    private void initBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if (col % 2 != row % 2) {
                    board[row][col] = new Piece(true);
                }
            }
        }
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (col % 2 != row % 2) {
                    board[row][col] = new Piece(false);
                }
            }
        }
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        if (board[startY][startX].getColor() == turn) {
            if (isLegalMove(startX, startY, endX, endY)) {
                board[endY][endX] = board[startY][startX];
                board[startY][startX] = null;
                turn = !turn;
                movesDone++;
                if (endY == 0 || endY == 7) board[endY][endX] = new King(turn);
                checkForMultiCapture(endX, endY);
                checkForDraw();
            }
        }
    }

    private boolean isLegalMove(int startX, int startY, int endX, int endY) {
        if (board[startY][startX] instanceof King) {
            if (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 1) {
                if (board[endY][endX] == null) return true;
            } else if (Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 2) {
                int midX = (startX + endX) / 2;
                int midY = (startY + endY) / 2;
                if (board[midY][midX] != null && board[midY][midX].getColor() != turn && board[endY][endX] == null)
                    return true;
            }
        } else {
            if (turn) {
                if (startX - endX == 1 && startY - endY == 1) {
                    if (board[endY][endX] == null) return true;
                } else if (startX - endX == 2 && startY - endY == 2) {
                    int midX = (startX + endX) / 2;
                    int midY = (startY + endY) / 2;
                    if (board[midY][midX] != null && board[midY][midX].getColor() != turn && board[endY][endX] == null)
                        return true;
                }
            } else {
                if (startX - endX == -1 && startY - endY == -1) {
                    if (board[endY][endX] == null) return true;
                } else if (startX - endX == -2 && startY - endY == -2) {
                    int midX = (startX + endX) / 2;
                    int midY = (startY + endY) / 2;
                    if (board[midY][midX] != null && board[midY][midX].getColor() != turn && board[endY][endX] == null)
                        return true;
                }
            }
        }
        return false;
    }

    private void checkForMultiCapture(int endX, int endY) {
        //check for possible captures in all directions
        if (board[endY][endX] instanceof King) {
            for (int dirY = -1; dirY <= 1; dirY += 2) {
                for (int dirX = -1; dirX <= 1; dirX += 2) {
                    int currX = endX;
                    int currY = endY;
                    while (true) {
                        currX += dirX;
                        currY += dirY;
                        if (currX < 0 || currX > 7 || currY < 0 || currY > 7) break;
                        if (board[currY][currX] != null && board[currY][currX].getColor() != turn) {
                            int midX = (currX + endX) / 2;
                            int midY = (currY + endY) / 2;
                            if (board[midY][midX] == null) break;
                            if (board[midY][midX] != null && board[midY][midX].getColor() != turn) {
                                if (currX + dirX < 0 || currX + dirX > 7 || currY + dirY < 0 || currY + dirY > 7) break;
                                if (board[currY + dirY][currX + dirX] == null) {
                                    board[currY][currX] = null;
                                    board[midY][midX] = null;
                                    movesDone++;
                                    endX = currX + dirX;
                                    endY = currY + dirY;
                                    board[endY][endX] = board[currY][currX];
                                    board[currY][currX] = null;
                                    checkForMultiCapture(endX, endY);
                                } else break;
                            }
                        } else break;
                    }
                }
            }
        } else {
            if (turn) {
                int dirY = 1;
                int dirX = 1;
                int currX = endX;
                int currY = endY;
                while (true) {
                    currX += dirX;
                    currY += dirY;
                    if (currX < 0 || currX > 7 || currY < 0 || currY > 7) break;
                    if (board[currY][currX] != null && board[currY][currX].getColor() != turn) {
                        int midX = (currX + endX) / 2;
                        int midY = (currY + endY) / 2;
                        if (board[midY][midX] == null) break;
                        if (board[midY][midX] != null && board[midY][midX].getColor() != turn) {
                            if (currX + dirX < 0 || currX + dirX > 7 || currY + dirY < 0 || currY + dirY > 7) break;
                            if (board[currY + dirY][currX + dirX] == null) {
                                board[currY][currX] = null;
                                board[midY][midX] = null;
                                movesDone++;
                                endX = currX + dirX;
                                endY = currY + dirY;
                                board[endY][endX] = board[currY][currX];
                                board[currY][currX] = null;
                                checkForMultiCapture(endX, endY);
                            } else break;
                        }
                    } else break;
                }
                dirY = 1;
                dirX = -1;
                currX = endX;
                currY = endY;
                while (true) {
                    currX += dirX;
                    currY += dirY;
                    if (currX < 0 || currX > 7 || currY < 0 || currY > 7) break;
                    if (board[currY][currX] != null && board[currY][currX].getColor() != turn) {
                        int midX = (currX + endX) / 2;
                        int midY = (currY + endY) / 2;
                        if (board[midY][midX] == null) break;
                        if (board[midY][midX] != null && board[midY][midX].getColor() != turn) {
                            if (currX + dirX < 0 || currX + dirX > 7 || currY + dirY < 0 || currY + dirY > 7) break;
                            if (board[currY + dirY][currX + dirX] == null) {
                                board[currY][currX] = null;
                                board[midY][midX] = null;
                                movesDone++;
                                endX = currX + dirX;
                                endY = currY + dirY;
                                board[endY][endX] = board[currY][currX];
                                board[currY][currX] = null;
                                checkForMultiCapture(endX, endY);
                            } else break;
                        }
                    } else break;
                }
            } else {
                int dirY = -1;
                int dirX = 1;
                int currX = endX;
                int currY = endY;
                while (true) {
                    currX += dirX;
                    currY += dirY;
                    if (currX < 0 || currX > 7 || currY < 0 || currY > 7) break;
                    if (board[currY][currX] != null && board[currY][currX].getColor() != turn) {
                        int midX = (currX + endX) / 2;
                        int midY = (currY + endY) / 2;
                        if (board[midY][midX] == null) break;
                        if (board[midY][midX] != null && board[midY][midX].getColor() != turn) {
                            if (currX + dirX < 0 || currX + dirX > 7 || currY + dirY < 0 || currY + dirY > 7) break;
                            if (board[currY + dirY][currX + dirX] == null) {
                                board[currY][currX] = null;
                                board[midY][midX] = null;
                                movesDone++;
                                endX = currX + dirX;
                                endY = currY + dirY;
                                board[endY][endX] = board[currY][currX];
                                board[currY][currX] = null;
                                checkForMultiCapture(endX, endY);
                            } else break;
                        }
                    } else break;
                }
                dirY = -1;
                dirX = -1;
                currX = endX;
                currY = endY;
                while (true) {
                    currX += dirX;
                    currY += dirY;
                    if (currX < 0 || currX > 7 || currY < 0 || currY > 7) break;
                    if (board[currY][currX] != null && board[currY][currX].getColor() != turn) {
                        int midX = (currX + endX) / 2;
                        int midY = (currY + endY) / 2;
                        if (board[midY][midX] == null) break;
                        if (board[midY][midX] != null && board[midY][midX].getColor() != turn) {
                            if (currX + dirX < 0 || currX + dirX > 7 || currY + dirY < 0 || currY + dirY > 7) break;
                            if (board[currY + dirY][currX + dirX] == null) {
                                board[currY][currX] = null;
                                board[midY][midX] = null;
                                movesDone++;
                                endX = currX + dirX;
                                endY = currY + dirY;
                                board[endY][endX] = board[currY][currX];
                                board[currY][currX] = null;
                                checkForMultiCapture(endX, endY);
                            } else break;
                        }
                    } else break;
                }
            }
        }
    }

    private void checkForDraw() {
        boolean canWhiteMove = false;
        boolean canBlackMove = false;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null && board[row][col].getColor()) {
                    if (isLegalMove(col, row, col + 1, row + 1) || isLegalMove(col, row, col - 1, row + 1) || isLegalMove(col, row, col + 2, row + 2) || isLegalMove(col, row, col - 2, row + 2))
                        canWhiteMove = true;
                } else if (board[row][col] != null && !board[row][col].getColor()) {
                    if (isLegalMove(col, row, col + 1, row - 1) || isLegalMove(col, row, col - 1, row - 1) || isLegalMove(col, row, col + 2, row - 2) || isLegalMove(col, row, col - 2, row - 2))
                        canBlackMove = true;
                }
                if (canWhiteMove && canBlackMove) return;
            }
        }
        gameOver = true;
    }
}
