package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class WumpusWorld extends JFrame implements ActionListener {
    private final int SIZE = 4;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private int wumpusX, wumpusY;
    private int goldX, goldY;
    private boolean[][] pits = new boolean[SIZE][SIZE];
    private boolean gameOver = false;

    public WumpusWorld() {
        setTitle("Thế Giới Wumpus");
        setLayout(new GridLayout(SIZE, SIZE));
        initializeGame();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setActionCommand(i + "," + j);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }

        // Khám phá ô (0, 0) ngay khi bắt đầu trò chơi
        exploreCell(0, 0);

        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeGame() {
        Random rand = new Random();
        wumpusX = rand.nextInt(SIZE);
        wumpusY = rand.nextInt(SIZE);
        goldX = rand.nextInt(SIZE);
        goldY = rand.nextInt(SIZE);

        // Đảm bảo Wumpus và vàng không ở cùng vị trí
        while (wumpusX == goldX && wumpusY == goldY) {
            goldX = rand.nextInt(SIZE);
            goldY = rand.nextInt(SIZE);
        }

        // Tạo hố ngẫu nhiên
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (rand.nextDouble() < 0.25) { // 25% xác suất tạo hố
                    pits[i][j] = true;
                }
            }
        }
    }

    private void exploreCell(int x, int y) {
        buttons[x][y].setBackground(Color.GREEN); // Đánh dấu ô đã khám phá
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        String command = e.getActionCommand();
        int x = Integer.parseInt(command.split(",")[0]);
        int y = Integer.parseInt(command.split(",")[1]);

        // Khám phá ô mới
        exploreCell(x, y);

        // Kiểm tra cảm giác gần Wumpus
        if (isNearWumpus(x, y)) {
            JOptionPane.showMessageDialog(this, "Bạn cảm thấy mùi hôi! Wumpus gần đây.");
        }

        // Kiểm tra cảm giác gần hố
        if (isNearPit(x, y)) {
            JOptionPane.showMessageDialog(this, "Bạn nghe thấy tiếng gió! Có hố gần đây.");
        }

        if (x == wumpusX && y == wumpusY) {
            JOptionPane.showMessageDialog(this, "Bạn gặp Wumpus! Kết thúc trò chơi.");
            gameOver = true;
        } else if (pits[x][y]) {
            JOptionPane.showMessageDialog(this, "Bạn đã rơi vào hố! Kết thúc trò chơi.");
            gameOver = true;
        } else if (x == goldX && y == goldY) {
            JOptionPane.showMessageDialog(this, "Bạn đã tìm thấy vàng! Bạn thắng!");
            gameOver = true;
        }
    }

    private boolean isNearWumpus(int x, int y) {
        return (x == wumpusX && Math.abs(y - wumpusY) == 1) || (y == wumpusY && Math.abs(x - wumpusX) == 1);
    }

    private boolean isNearPit(int x, int y) {
        return (x > 0 && pits[x - 1][y]) || (x < SIZE - 1 && pits[x + 1][y]) ||
               (y > 0 && pits[x][y - 1]) || (y < SIZE - 1 && pits[x][y + 1]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WumpusWorld::new);
    }
}
