import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CheckersBoard extends JPanel implements MouseListener {

    private Game game;

    public CheckersBoard() {
        this.setPreferredSize(new Dimension(600, 600));
        this.addMouseListener(this);
        this.game = new Game();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = col * 75;
                int y = row * 75;

                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x, y, 75, 75);
            }
        }

        //draw the pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = col * 75;
                int y = row * 75;

                if (game.getBoard().getPiece(row, col) == Board.RED) {
                    g.setColor(Color.RED);
                    g.fillOval(x + 10, y + 10, 55, 55);
                }
                else if (game.getBoard().getPiece(row, col) == Board.BLACK) {
                    g.setColor(Color.BLACK);
                    g.fillOval(x + 10, y + 10, 55, 55);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = e.getX() / 75;
        int row = e.getY() / 75;

        game.playerTurn(row, col);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new CheckersBoard());
        frame.pack();
        frame.setVisible(true);
    }

}

class Game {

    private Board board;
    private Player currentPlayer;

    public Game() {
        this.board = new Board();
        this.currentPlayer = Player.RED;
    }

    public void playerTurn(int row, int col) {
        board.placePiece(row, col, currentPlayer);

        if (currentPlayer == Player.RED) {
            currentPlayer = Player.BLACK;
        } else {
            currentPlayer = Player.RED;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }
}

class Board {

    public static final int RED = 1;
    public static final int BLACK = 2;
    public static final int EMPTY = 0;

    private int[][] pieces;

    public Board() {
        this.pieces = new int[8][8];

        //setup the board
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    pieces[row][col] = RED;
                }
            }
        }

        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    pieces[row][col] = BLACK;
                }
            }
        }
    }

    public void placePiece(int row, int col, Player player) {
        if (player == Player.RED) {
            pieces[row][col] = RED;
        } else {
            pieces[row][col] = BLACK;
        }
    }

    public int getPiece(int row, int col) {
        return pieces[row][col];
    }

}

enum Player {
    RED, BLACK;
}