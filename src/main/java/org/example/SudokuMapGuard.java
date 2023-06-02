package org.example;

import java.util.Arrays;

import static org.example.SudokuMapOld.SQUARE_SIZE;

public class SudokuMapGuard {
    public final boolean[][] clashes;
    private final int[][] fields;
    private final int size;
    private final int squareSize;

    public SudokuMapGuard(int[][] fields) {
        this(fields, SQUARE_SIZE);
    }

    public SudokuMapGuard(int[][] fields, int squareSize) {
        this.size = fields.length;
        this.squareSize = squareSize;
        this.fields = fields;
        this.clashes = new boolean[size][size];
        this.resetClashes();
    }

    private void resetClashes() {
        for (boolean[] row : clashes) {
            Arrays.fill(row, false);
        }
    }

    public boolean findClashes(int row, int column, int value) {
        resetClashes();
        boolean rowClash = findRowClash(row, value);
        boolean columnClash = findColumnClash(column, value);
        boolean squareClash = findSquareClash(row, column, value);
        return rowClash || columnClash || squareClash;
    }


    private boolean findRowClash(int row, int value) {
        for (int i = 0; i < size; i++) {
            if (fields[row][i] == value) {
                clashes[row][i] = true;
                return true;
            }
        }
        return false;
    }

    private boolean findColumnClash(int column, int value) {
        for (int i = 0; i < size; i++) {
            if (fields[i][column] == value) {
                clashes[i][column] = true;
                return true;
            }
        }
        return false;
    }

    private boolean findSquareClash(int row, int column, int value) {
        for (int i = 0; i < squareSize; i++) {
            for (int j = 0; j < squareSize; j++) {
                int y = (row / 3) * 3;
                int x = (column / 3) * 3;
                if (fields[y][x] == value) {
                    clashes[y][x] = true;
                    return true;
                }
            }
        }
        return false;
    }
}
