package Game.Board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Game.Pieces.Advisor;
import Game.Pieces.Bishop;
import Game.Pieces.Cannon;
import Game.Pieces.King;
import Game.Pieces.Knight;
import Game.Pieces.Pawn;
import Game.Pieces.Piece;
import Game.Pieces.Rook;
import Game.Player.BlackPlayer;
import Game.Player.Player;
import Game.Player.RedPlayer;
import Game.Side;

public class Board {
    private static List<Tile> gameBoard;
    private final Collection<Piece> redPieces;
    private final Collection<Piece> blackPieces;

    public final RedPlayer redPlayer;
    private final BlackPlayer blackPlayer;

    private final Player currentPlayer;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.redPieces = calculateActivePieces(this.gameBoard, Side.Red);
        this.blackPieces = calculateActivePieces(this.gameBoard, Side.Black);

        final Collection<Move> redStandardLegalMoves = calculateLegalMoves(this.redPieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.redPlayer = new RedPlayer(this, redStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, redStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextTurn.choosePlayer(this.redPlayer, this.blackPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player redPlayer() {
        return this.redPlayer;
    }

    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getRedPieces() {
        return this.redPieces;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.legalMoves(this));
        }
        return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Side side) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Tile tile : gameBoard) {
            if (tile.isOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getSide() == side) {
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    public static Tile getTile(final int tileCoord) {
        return gameBoard.get(tileCoord);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final List<Tile> tiles = new ArrayList<>(BoardUtils.NUM_TILES);
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        return tiles;
    }

    public Collection<Move> getAllLegalMoves() {
        return Stream.concat(this.redPlayer.getLegalMoves().stream(),
                this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList());
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // RED
        builder.setPiece(new Rook(0, Side.Red));
        builder.setPiece(new Rook(8, Side.Red));
        builder.setPiece(new Knight(1, Side.Red));
        builder.setPiece(new Knight(7, Side.Red));
        builder.setPiece(new Bishop(2, Side.Red));
        builder.setPiece(new Bishop(6, Side.Red));
        builder.setPiece(new Advisor(3, Side.Red));
        builder.setPiece(new Advisor(5, Side.Red));
        builder.setPiece(new King(4, Side.Red));
        builder.setPiece(new Cannon(19, Side.Red));
        builder.setPiece(new Cannon(25, Side.Red));
        builder.setPiece(new Pawn(27, Side.Red));
        builder.setPiece(new Pawn(29, Side.Red));
        builder.setPiece(new Pawn(31, Side.Red));
        builder.setPiece(new Pawn(33, Side.Red));
        builder.setPiece(new Pawn(35, Side.Red));
        // Black
        builder.setPiece(new Rook(89, Side.Black));
        builder.setPiece(new Rook(81, Side.Black));
        builder.setPiece(new Knight(88, Side.Black));
        builder.setPiece(new Knight(82, Side.Black));
        builder.setPiece(new Bishop(87, Side.Black));
        builder.setPiece(new Bishop(83, Side.Black));
        builder.setPiece(new Advisor(86, Side.Black));
        builder.setPiece(new Advisor(84, Side.Black));
        builder.setPiece(new King(85, Side.Black));
        builder.setPiece(new Cannon(64, Side.Black));
        builder.setPiece(new Cannon(70, Side.Black));
        builder.setPiece(new Pawn(54, Side.Black));
        builder.setPiece(new Pawn(56, Side.Black));
        builder.setPiece(new Pawn(58, Side.Black));
        builder.setPiece(new Pawn(60, Side.Black));
        builder.setPiece(new Pawn(62, Side.Black));
        // Red to move
        builder.setTurn(Side.Red);
        return builder.build();
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Side nextTurn;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Board build() {
            return new Board(this);
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this;
        }

        public Builder setTurn(final Side nextTurn) {
            this.nextTurn = nextTurn;
            return this;
        }
    }
}
