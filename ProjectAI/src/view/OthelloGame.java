package view;

import javax.swing.*;
import java.awt.*;

public class OthelloGame extends JFrame {
    private JButton[][] boardButtons;
    private JButton startButton;
    private JButton hintButton; 
    private JButton skipButton; 
    private JTextArea messageArea;
    private char currentPlayer;
    private char[][] board;
    private int countX; 
    private int countO; 

    public OthelloGame() {
        setTitle("Game Othello");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize the game board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8));
        boardButtons = new JButton[8][8];
        board = new char[8][8];
        currentPlayer = 'X';

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setFont(new Font("Arial", Font.PLAIN, 30));
                boardButtons[i][j].setFocusPainted(false);
                boardButtons[i][j].setBackground(Color.GREEN);
                int row = i;
                int col = j;
                boardButtons[i][j].addActionListener(e -> {
                    makeMove(row, col);
                });
                boardPanel.add(boardButtons[i][j]);
            }
        }

        // Control buttons
        startButton = new JButton("Bắt đầu lại");
        startButton.addActionListener(e -> resetBoard());

        hintButton = new JButton("Gợi ý");
        hintButton.addActionListener(e -> showHints());

        skipButton = new JButton("Bỏ qua lượt");
        skipButton.addActionListener(e -> skipTurn());

        // Message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(Color.WHITE);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        updateMessage(); 

        // Add components to frame
        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);
        controlPanel.add(hintButton);
        controlPanel.add(skipButton); 

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(new JScrollPane(messageArea), BorderLayout.EAST);

        resetBoard();
        setVisible(true);
    }

    private void makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayer;
            boardButtons[row][col].setText(String.valueOf(currentPlayer));
            flipDiscs(row, col);
            updateCounts();
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch player
            clearHints(); 
            updateMessage(); 
        } else {
            messageArea.setText("Nước đi không hợp lệ! Thử lại.");
        }
    }

    private void skipTurn() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; 
        messageArea.setText("Lượt chơi của: " + currentPlayer);
        updateMessage(); // Update message
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8 || board[row][col] != '\0') {
            return false;
        }
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';

        for (int dirX = -1; dirX <= 1; dirX++) {
            for (int dirY = -1; dirY <= 1; dirY++) {
                if (dirX == 0 && dirY == 0) continue;

                int x = row + dirX;
                int y = col + dirY;
                boolean foundOpponent = false;

                while (isInBounds(x, y)) {
                    if (board[x][y] == opponent) {
                        foundOpponent = true;
                    } else if (board[x][y] == currentPlayer) {
                        if (foundOpponent) return true; 
                        break; 
                    } else {
                        break; 
                    }
                    x += dirX;
                    y += dirY;
                }
            }
        }
        return false; 
    }

    private void flipDiscs(int row, int col) {
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';

        for (int dirX = -1; dirX <= 1; dirX++) {
            for (int dirY = -1; dirY <= 1; dirY++) {
                if (dirX == 0 && dirY == 0) continue;

                int x = row + dirX;
                int y = col + dirY;
                boolean foundOpponent = false;

                while (isInBounds(x, y)) {
                    if (board[x][y] == opponent) {
                        foundOpponent = true;
                    } else if (board[x][y] == currentPlayer) {
                        if (foundOpponent) {
                            x -= dirX;
                            y -= dirY;
                            while (board[x][y] == opponent) {
                                board[x][y] = currentPlayer;
                                boardButtons[x][y].setText(String.valueOf(currentPlayer));
                                x -= dirX;
                                y -= dirY;
                            }
                        }
                        break;
                    } else {
                        break;
                    }
                    x += dirX;
                    y += dirY;
                }
            }
        }
    }

    private void updateCounts() {
        countX = 0;
        countO = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'X') {
                    countX++;
                } else if (board[i][j] == 'O') {
                    countO++;
                }
            }
        }
    }

    private void updateMessage() {
        messageArea.setText("Người chơi X: " + countX + " quân, Người chơi O: " + countO + " quân. Lượt chơi của: " + currentPlayer);
    }

    private void showHints() {
        clearHints();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j)) {
                    boardButtons[i][j].setBackground(Color.YELLOW);
                }
            }
        }
    }

    private void clearHints() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardButtons[i][j].getText().isEmpty()) {
                    boardButtons[i][j].setBackground(Color.GREEN);
                }
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardButtons[i][j].setText("");
                board[i][j] = '\0';
                boardButtons[i][j].setEnabled(true);
            }
        }
        board[3][3] = 'X';
        board[3][4] = 'O';
        board[4][3] = 'O';
        board[4][4] = 'X';
        boardButtons[3][3].setText("X");
        boardButtons[3][4].setText("O");
        boardButtons[4][3].setText("O");
        boardButtons[4][4].setText("X");
        updateCounts(); 
        updateMessage(); 
        currentPlayer = 'X';
    }
    // hàm herictis
    // hàm minimax, update lại code

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OthelloGame::new);
    }
}
