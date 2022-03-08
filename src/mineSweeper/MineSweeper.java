package mineSweeper;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import java.awt.*;

public class MineSweeper {
    private static final int NUM_MINES = 100;
    private static final int SIZE = 20;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mine Sweeper | # of mines: " + NUM_MINES);
        frame.add(new MineSweeperGUI(SIZE, SIZE, NUM_MINES));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}

class MineSweeperGUI extends JPanel {
    private MineGrid grid;

    public MineSweeperGUI(int numRows, int numCols, int numMines) {
        grid = new MineGrid(numRows, numCols, numMines);

        setLayout(new GridLayout(numRows, numCols));
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                JButton button = new JButton();
                add(button);
                button.addActionListener(new ButtonHandler(i,j, grid));
            }
        }
    }
}

class ButtonHandler implements ActionListener {
    private int row, col;
    private MineGrid grid;

    public ButtonHandler(int x, int y, MineGrid g) {
        row = x;
        col = y;
        grid = g;
    }

    public void actionPerformed(ActionEvent event) {
        if(grid.containsMineAt(row, col)) {
            JOptionPane.showMessageDialog(null, "OOOPS!!");
            System.exit(0);
        } else {
            JButton button = (JButton)event.getSource();
            button.setText(String.valueOf(grid.getInfoAt(row, col)));
        }
    }
}

class MineGrid {
    public static final int MINE = -1;
    protected int[][] mineInformation;
    // mine information is either MINE, meaning there is a mine
    // at that cell, or it keeps the number of surrounding mines.

    public MineGrid(int numRows, int numCols, int numMines) {
        mineInformation = new int[numRows][numCols];

        initializeCells();
        placeMines(numMines);
        setMineInformation();
    }

    private void initializeCells() {
        for(int i = 0; i < mineInformation.length; i++) {
            for(int j = 0; j < mineInformation[0].length; j++) {
                mineInformation[i][j] = 0;
            }
        }
    }

    private void placeMines(int numMines) {
        Random random = new Random();
        for(int i = 0; i < numMines; i++) {
            int r = random.nextInt(mineInformation.length);
            int c = random.nextInt(mineInformation[0].length);
            mineInformation[r][c] = MINE;
        }
    }

    private void setMineInformation() {
        for(int i = 0; i < mineInformation.length; i++) {
            for(int j = 0; j < mineInformation[0].length; j++) {
                if(mineInformation[i][j] == MINE) {
                    // previous row
                    incrementMineCountAt(i-1, j-1);
                    incrementMineCountAt(i-1, j);
                    incrementMineCountAt(i-1, j+1);

                    // left and right cells
                    incrementMineCountAt(i, j-1);
                    incrementMineCountAt(i, j+1);

                    // next row
                    incrementMineCountAt(i+1, j-1);
                    incrementMineCountAt(i+1, j);
                    incrementMineCountAt(i+1, j+1);
                }
            }
        }
    }

    private void incrementMineCountAt(int i, int j) {
        if(isInsideGrid(i, j) && mineInformation[i][j] != MINE) {
            mineInformation[i][j]++;
        }
    }

    private boolean isInsideGrid(int i, int j) {
        return (i >= 0 && i < mineInformation.length) &&
                (j >= 0 && j < mineInformation[0].length);
    }

    public int getInfoAt(int i, int j) {
        return mineInformation[i][j];
    }

    public boolean containsMineAt(int i, int j) {
        return getInfoAt(i, j) == MINE;
    }
}
