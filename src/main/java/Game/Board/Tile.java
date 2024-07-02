package Game.Board;

import java.util.HashMap;
import java.util.Map;

import Game.Pieces.Piece;

public abstract class Tile {
    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllEmpty();

    private static Map<Integer, EmptyTile> createAllEmpty() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return emptyTileMap;
    }

    public static Tile createTile(final int coord, final Piece piece) {
        return piece != null ? new OccupiedTile(coord, piece) : EMPTY_TILES.get(coord);
    }

    public Tile(int coord) {
        this.tileCoordinate = coord;
    }

    public abstract Piece getPiece();

    public abstract boolean isOccupied();

    public static final class EmptyTile extends Tile {

        private EmptyTile(final int coord) {
            super(coord);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
        private final Piece currentPiece;

        private OccupiedTile(final int coord, Piece currentPiece) {
            super(coord);
            this.currentPiece = currentPiece;
        }

        @Override
        public String toString() {
            return getPiece().getSide().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }

        @Override
        public boolean isOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.currentPiece;
        }

    }
}
