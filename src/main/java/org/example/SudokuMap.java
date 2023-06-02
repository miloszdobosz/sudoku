package org.example;

import java.util.Arrays;
import java.util.Random;

public class SudokuMap {
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
    private final int[][] fields;
    private final boolean[][] mutable;
    private final SudokuMapGuard guard;

    private final SudokuMapView view;
    private int occupiedCount;

    public SudokuMap(int percentToFill) {
        this.solution = initialSolution;
        this.shuffleSolution();

        this.occupiedCount = SIZE * SIZE * percentToFill / 100;

        this.fields = new int[SIZE][SIZE];
        this.mutable = new boolean[SIZE][SIZE];
        this.fill();

        this.guard = new SudokuMapGuard(fields);
        this.view = new SudokuMapView(fields, mutable, guard.clashes);
    }

    private void fill() {
        Random random = new Random();

        for (boolean[] row : mutable) {
            Arrays.fill(row, true);
        }

        for (int i = 0; i < occupiedCount; i++) {
            int row = random.nextInt(SIZE);
            int column = random.nextInt(SIZE);

            fields[row][column] = solution[row][column];
            mutable[row][column] = false;
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
            int temp = solution[first][j];
            solution[first][j] = solution[second][j];
            solution[second][j] = temp;
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
        return occupiedCount == SIZE * SIZE;
    }

    public void put(int row, int column, int value) {
        if (fields[row][column] == value) {
            return;
        }
        if (!mutable[row][column]) {
            throw new IllegalArgumentException("Cannot change generated fields!");
        }
        if (guard.findClashes(row, column, value)) {
            throw new IllegalArgumentException("Clashes!");
        }
        if (value == 0) {
            occupiedCount--;
        } else {
            occupiedCount++;
        }
        fields[row][column] = value;
    }

    public String toString() {
        return view.toString();
    }
}