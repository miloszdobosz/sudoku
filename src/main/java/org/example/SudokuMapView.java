package org.example;

import static org.example.SudokuMap.SQUARE_SIZE;

public class SudokuMapView {
    private final int[][] fields;
    private final boolean[][] okMask;
    private final boolean[][] errorMask;
    private final int size;
    private final int squareSize;

    public SudokuMapView(int[][] fields, boolean[][] okMask, boolean[][] errorMask) {
        this(fields, okMask, errorMask, SQUARE_SIZE);
    }
    public SudokuMapView(int[][] fields, boolean[][] okMask, boolean[][] errorMask, int squareSize) {
        this.fields = fields;
        this.okMask = okMask;
        this.errorMask = errorMask;
        this.size = fields.length;
        this.squareSize = squareSize;
    }

    static String getColoredString(String s, COLORS color) {
        return color.toString() + s + COLORS.RESET;
    }

    private String getFieldString(int row, int column) {
        return getColoredString(String.valueOf(fields[row][column]), getFieldColor(row, column));
    }

    private COLORS getFieldColor(int row, int column) {
        if (errorMask[row][column]) {
            return COLORS.ERROR;
        } else if (okMask[row][column]) {
            return COLORS.OK;
        } else {
            return COLORS.RESET;
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i % squareSize == 0) {
                result.append("\n");
            }
            result.append("    ");
            for (int j = 0; j < size; j++) {
                if (j % 3 == 0) {
                    result.append("  ");
                }
                if (fields[i][j] == 0) {
                    result.append(".");
                } else {
                    result.append(getFieldString(i, j));
                }
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public enum COLORS {
        CHOICE,
        OK,
        ERROR,
        RESET;

        @Override
        public String toString() {
            switch (this) {
                case CHOICE -> {
                    return "\u001b[33m";
                }
                case OK -> {
                    return "\u001b[32m";
                }
                case ERROR -> {
                    return "\u001b[31m";
                }
            }
            return "\u001b[0m";
        }
    }


}
