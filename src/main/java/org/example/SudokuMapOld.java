package org.example;

import java.util.Random;

public class SudokuMapOld {
    public static final int SIZE = 9;
    public static final int SQUARE_SIZE = 3;
    public static final int SHUFFLE_LOOPS = 100;
    private static final int[][] initialSolution = new int[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9},
            {4, 5, 6, 7, 8, 9, 1, 2, 3},
            {7, 8, 9, 1, 2, 3, 4, 5, 6},
            {2, 3, 4, 5, 6, 7, 8, 9, 1},
            {5, 6, 7, 8, 9, 1, 2, 3, 4},
            {8, 9, 1, 2, 3, 4, 5, 6, 7},
            {3, 4, 5, 6, 7, 8, 9, 1, 2},
            {6, 7, 8, 9, 1, 2, 3, 4, 5},
            {9, 1, 2, 3, 4, 5, 6, 7, 8},
    };
    private final int[][] solution;
    private final boolean[][] generated;
    private final int[][] fields;
    private final GroupGuardOld rows = new GroupGuardOld();
    private final GroupGuardOld columns = new GroupGuardOld();
    private final GroupGuardOld squares = new GroupGuardOld();
    private int occupied;

    public SudokuMapOld(int percentToFill) {
        this.solution = initialSolution;
        this.shuffleSolution();
        this.occupied = SIZE * SIZE * percentToFill / 100;
        this.generated = new boolean[SIZE][SIZE];
        this.fields = new int[SIZE][SIZE];
        this.fill();
    }

    private void fill() {
        Random random = new Random();

        for (int i = 0; i < this.occupied; i++) {
            int row = random.nextInt(9);
            int column = random.nextInt(9);

            this.generated[row][column] = true;
            this.fields[row][column] = this.solution[row][column];
            this.addToGroups(row, column, this.solution[row][column]);
        }
    }

    private void shuffleSolution() {
        Random random = new Random();

        // Shuffle rows and columns within the square groups to not break them
        for (int i = 0; i < SHUFFLE_LOOPS; i++) {
            int group = random.nextInt(3);
            int index = random.nextInt(3);

            int first = getFieldIndex(group, index);
            int second = getFieldIndex(group, index + 1);
            swapSolutionRows(first, second);

            group = random.nextInt(3);
            index = random.nextInt(3);

            first = getFieldIndex(group, index);
            second = getFieldIndex(group, index + 1);
            swapSolutionColumns(first, second);
        }
    }

    private int getFieldIndex(int group, int which) {
        return group * 3 + which % 3;
    }

    private void swapSolutionRows(int first, int second) {
        for (int j = 0; j < SIZE; j++) {
            int temp = this.solution[first][j];
            solution[first][j] = solution[second][j];
            this.solution[second][j] = temp;
        }
    }

    private void swapSolutionColumns(int first, int second) {
        for (int j = 0; j < SIZE; j++) {
            int temp = solution[j][first];
            solution[j][first] = solution[j][second];
            solution[j][second] = temp;
        }
    }

    public boolean checkWon() {
        return occupied == SIZE * SIZE;
    }

    private void addToGroups(int row, int column, int value) {
        this.rows.add(row, value);
        this.columns.add(column, value);
        this.squares.add(this.getGroupSquares(row, column), value);
    }

    private void removeFromGroups(int row, int column) {
        int value = this.fields[row][column];
        this.rows.remove(column, value);
        this.columns.remove(row, value);
        this.squares.remove(this.getGroupSquares(row, column), value);
    }

    private int getGroupSquares(int row, int column) {
        return row / 3 * 3 + column / 3;
    }

    public void put(int row, int column, int value) {
        // TODO ta sama co jest
        // TODO zmiana nie usuwa z grup
        if (this.fields[row][column] == value) {
            return;
        }
        if (this.generated[row][column]) {
            throw new IllegalArgumentException("Cannot change generated fields!");
        }
        if (this.rows.contains(row, value)) {
            throw new IllegalArgumentException("Row already contains this!");
        }
        if (this.columns.contains(column, value)) {
            throw new IllegalArgumentException("Column already contains this!");
        }
        if (this.squares.contains(this.getGroupSquares(row, column), value)) {
            throw new IllegalArgumentException("Square already contains this!!");
        }
        if (value == 0) {
//            System.out.println("o co cho");
            this.removeFromGroups(row, column);
            this.occupied--;
        } else {
            this.addToGroups(row, column, value);
            this.occupied++;
        }
        this.fields[row][column] = value;
    }

    public void printSolution() {
        System.out.println(this.tableToString(this.solution));
    }

    private String tableToString(int[][] table) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0) {
                result.append("\n");
            }
            result.append("    ");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0) {
                    result.append("  ");
                }
                if (this.fields[j][i] == 0) {
                    result.append(".");
                } else {
                    if (generated[j][i]) {
                        result.append(this.fields[j][i]);
                    } else {
                        result.append("\u001B[32m");
                        result.append(this.fields[j][i]);
                        result.append("\u001B[0m");
                    }
                }
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String toString() {
        return tableToString(this.fields);
    }
}