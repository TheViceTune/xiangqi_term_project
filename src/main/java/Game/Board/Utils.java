package Game.Board;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Game.Side;

public class Utils {

    public static final boolean[] FIRST = initColumn(0);
    public static final boolean[] SECOND = initColumn(1);
    public static final boolean[] THIRD = initColumn(2);
    public static final boolean[] FOURTH = initColumn(3);
    public static final boolean[] FIFTH = initColumn(4);
    public static final boolean[] SIXTH = initColumn(5);
    public static final boolean[] SEVENTH = initColumn(6);
    public static final boolean[] EIGHTH = initColumn(7);
    public static final boolean[] NINTH = initColumn(8);
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

    public static final List<String> ALGEBRAIC_NOTATION = makeBoardCoord();
    public static final Map<String, Integer> POSITION_TO_COORD = makeBoardCoordMap();

    public static final int numberOfTiles = 90;
    public static final int tilesPerRow = 9;
    public static final int tilesPerColumn = 10;

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[numberOfTiles];
        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += tilesPerRow;
        } while (columnNumber < numberOfTiles);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[numberOfTiles];
        Arrays.fill(row, false);
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % tilesPerRow != 0);
        return row;
    }

    private static List<String> makeBoardCoord() {
        return Arrays.asList(
                "a10", "b10", "c10", "d10", "e10", "f10", "g10", "h10", "i10",
                "a9", "b9", "c9", "d9", "e9", "f9", "g9", "h9", "i9",
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "i8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "i7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "i6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "i5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "i4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "i3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "i1");
    }

    private static Map<String, Integer> makeBoardCoordMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < numberOfTiles; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return positionToCoordinate;
    }

    public static boolean isValid(int coord) {
        return coord >= 0 && coord < numberOfTiles;
    }

    public static boolean isBeforeRiver(int coord, Side pieceSide) {
        if (coord <= 44 && pieceSide == Side.Red) {
            return true;
        } else if (coord >= 44 && pieceSide == Side.Black) {
            return true;
        }
        return false;
    }

    public static int getCoordAtPosition(final String position) {
        return POSITION_TO_COORD.get(position);
    }

    public static String getPositionAtCoord(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
}