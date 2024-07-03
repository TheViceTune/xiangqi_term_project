package Game.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Game.Board.Board;
import Game.Board.Move;
import Game.Board.Move.AttackMove;
import Game.Board.Move.MainMove;
import Game.Board.Tile;
import Game.Board.Utils;
import Game.Side;

public class Horse extends Piece {

    private final static int[] NEXT = { -19, -17, -11, -7, 7, 11, 17, 19 };

    public Horse(final int position, final Side pieceSide) {
        super(TypePiece.HORSE, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int potentialTile;

        for (final int nextCoord : NEXT) {
            potentialTile = this.position + nextCoord;
            if (Utils.isValid(potentialTile)) {
                if (excludeFirst(this.position, nextCoord) ||
                        excludeSecond(this.position, nextCoord) ||
                        excludeEighth(this.position, nextCoord) ||
                        excludeNinth(this.position, nextCoord) ||
                        isBlocked(board, position, nextCoord)) {
                    continue;
                }
                final Tile candidateTile = Board.getTile(potentialTile);

                if (!candidateTile.isOccupied()) {
                    legalMoves.add(new MainMove(board, this, potentialTile));
                } else {
                    final Piece atDestination = candidateTile.getPiece();
                    final Side destinationSide = atDestination.getSide();

                    if (this.pieceSide != destinationSide) {
                        legalMoves.add(new AttackMove(board, this, potentialTile, atDestination));
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Horse movePiece(Move move) {
        return new Horse(move.getDestCoord(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return TypePiece.HORSE.toString();
    }

    public static boolean excludeFirst(final int position, final int candidateOffset) {
        return Utils.FIRST[position] && ((candidateOffset == -19) || (candidateOffset == -11)
                || (candidateOffset == 7) || (candidateOffset == 17));
    }

    public static boolean excludeSecond(final int position, final int candidateOffset) {
        return Utils.SECOND[position] && ((candidateOffset == 7) || (candidateOffset == -11));
    }

    public static boolean excludeEighth(final int position, final int candidateOffset) {
        return Utils.EIGHTH[position] && ((candidateOffset == -7) || (candidateOffset == 11));
    }

    public static boolean excludeNinth(final int position, final int candidateOffset) {
        return Utils.NINTH[position] && ((candidateOffset == 19) || (candidateOffset == 11)
                || (candidateOffset == -7) || (candidateOffset == -17));
    }

    public static boolean isBlocked(final Board board, final int position, final int candidateOffset) {
        switch (candidateOffset) {
            case -19:
            case -17:
                return board.getTile(position - 9).isOccupied();
            case -11:
            case 7:
                return board.getTile(position - 1).isOccupied();
            case -7:
            case 11:
                return board.getTile(position + 1).isOccupied();
            case 17:
            case 19:
                return board.getTile(position + 9).isOccupied();
            default:
                return false;
        }
    }
}
