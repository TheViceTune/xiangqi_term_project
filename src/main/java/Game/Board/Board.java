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
import Game.Pieces.General;
import Game.Pieces.Horse;
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

    private Board(BoardBuilder builder) {
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
        for (int i = 0; i < Utils.numberOfTiles; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % Utils.tilesPerRow == 0) {
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

    private static List<Tile> createGameBoard(final BoardBuilder builder) {
        final List<Tile> tiles = new ArrayList<>(Utils.numberOfTiles);
        for (int i = 0; i < Utils.numberOfTiles; i++) {
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        return tiles;
    }

    public Collection<Move> getAllLegalMoves() {
        return Stream.concat(this.redPlayer.getLegalMoves().stream(),
                this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList());
    }

    public static Board createStandardBoard() {
        final BoardBuilder builder = new BoardBuilder();
        // RED
        builder.pieceSet(new Rook(0, Side.Red));
        builder.pieceSet(new Rook(8, Side.Red));
        builder.pieceSet(new Horse(1, Side.Red));
        builder.pieceSet(new Horse(7, Side.Red));
        builder.pieceSet(new Bishop(2, Side.Red));
        builder.pieceSet(new Bishop(6, Side.Red));
        builder.pieceSet(new Advisor(3, Side.Red));
        builder.pieceSet(new Advisor(5, Side.Red));
        builder.pieceSet(new General(4, Side.Red));
        builder.pieceSet(new Cannon(19, Side.Red));
        builder.pieceSet(new Cannon(25, Side.Red));
        builder.pieceSet(new Pawn(27, Side.Red));
        builder.pieceSet(new Pawn(29, Side.Red));
        builder.pieceSet(new Pawn(31, Side.Red));
        builder.pieceSet(new Pawn(33, Side.Red));
        builder.pieceSet(new Pawn(35, Side.Red));
        // Black
        builder.pieceSet(new Rook(89, Side.Black));
        builder.pieceSet(new Rook(81, Side.Black));
        builder.pieceSet(new Horse(88, Side.Black));
        builder.pieceSet(new Horse(82, Side.Black));
        builder.pieceSet(new Bishop(87, Side.Black));
        builder.pieceSet(new Bishop(83, Side.Black));
        builder.pieceSet(new Advisor(86, Side.Black));
        builder.pieceSet(new Advisor(84, Side.Black));
        builder.pieceSet(new General(85, Side.Black));
        builder.pieceSet(new Cannon(64, Side.Black));
        builder.pieceSet(new Cannon(70, Side.Black));
        builder.pieceSet(new Pawn(54, Side.Black));
        builder.pieceSet(new Pawn(56, Side.Black));
        builder.pieceSet(new Pawn(58, Side.Black));
        builder.pieceSet(new Pawn(60, Side.Black));
        builder.pieceSet(new Pawn(62, Side.Black));
        // Red to move
        builder.setTurn(Side.Red);
        return builder.build();
    }

    public static class BoardBuilder {
        Map<Integer, Piece> boardConfig;
        Side nextTurn;

        public BoardBuilder() {
            this.boardConfig = new HashMap<>();
        }

        public Board build() {
            return new Board(this);
        }

        public BoardBuilder pieceSet(final Piece piece) {
            this.boardConfig.put(piece.getPosition(), piece);
            return this;
        }

        public BoardBuilder setTurn(final Side nextTurn) {
            this.nextTurn = nextTurn;
            return this;
        }
    }
}
