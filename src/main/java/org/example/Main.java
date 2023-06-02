package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.example.SudokuMap.SIZE;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n-=| WELCOME TO THE DOJO |=-");
        SudokuMap sudokuMap = new SudokuMap(readInt(reader, "-=| Percent to fill: ", 0, 100));

        while (!sudokuMap.checkWon()) {
            System.out.println(sudokuMap);

            int row = readInt(reader, "-=| Y:\t", 0, SIZE);
            int column = readInt(reader, "-=| X:\t", 0, SIZE);
            int value = readInt(reader, "-=| Value:\t", 0, SIZE);

            System.out.println();

            try {
                sudokuMap.put(row, column, value);
            } catch (Exception e) {
                err(e.getMessage());
            }
        }

        System.out.println("-=| WON! CONGRATULATIONS!");
    }

    static private int readInt(BufferedReader reader, String prompt, int lowerBound, int upperBound) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(reader.readLine());
                if (value < lowerBound || value > upperBound) {
                    err("Value not within [" + lowerBound + ", " + upperBound + "]");
                } else {
                    return value;
                }
            } catch (Exception e) {
                err("Value not a number!");
            }
        }
    }

    static void err(String message) {
        System.out.println("    " + SudokuMapView.getColoredString(message, SudokuMapView.COLORS.ERROR));
    }
}