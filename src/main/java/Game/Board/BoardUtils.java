package Game.Board;

import java.util.Arrays;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] THIRD_COLUMN = initColumn(2);
    public static final boolean[] FOURTH_COLUMN = initColumn(3);
    public static final boolean[] FIFTH_COLUMN = initColumn(4);
    public static final boolean[] SIXTH_COLUMN = initColumn(5);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] NINTH_COLUMN = initColumn(8);
    public static final boolean[] TENTH_RANK = initRow(0);
    public static final boolean[] NINTH_RANK = initRow(9);
    public static final boolean[] EIGHTH_RANK = initRow(18);
    public static final boolean[] SEVENTH_RANK = initRow(27);
    public static final boolean[] SIXTH_RANK = initRow(36);
    public static final boolean[] FIFTH_RANK = initRow(45);
    public static final boolean[] FOURTH_RANK = initRow(54);
    public static final boolean[] THIRD_RANK = initRow(63);
    public static final boolean[] SECOND_RANK = initRow(72);
    public static final boolean[] FIRST_RANK = initRow(81);

    public static final int NUM_TILES = 90;
    public static final int NUM_TILES_PER_ROW = 9;
    public static final int NUM_TILES_PER_COLUMN = 10;

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        Arrays.fill(row, false);
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    public static boolean isValid(int coord) {
        return coord >= 0 && coord < NUM_TILES;
    }

    public static boolean isBeforeRiver(int coord) {
        return coord <= 44;
    }
}