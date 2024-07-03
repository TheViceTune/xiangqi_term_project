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

public class Advisor extends Piece {
    private final int[] NEXT;

    public Advisor(final int position, final Side pieceSide) {
        super(TypePiece.ADVISOR, position, pieceSide);
        this.NEXT = determineCandidateOffsets(this.position);
    }

    private int[] determineCandidateOffsets(int position) {
        if (position == 13 || position == 76) {
            return new int[] { -10, -8, 8, 10 };
        } else if (position == 3 || position == 66) {
            return new int[] { 10 };
        } else if (position == 5 || position == 68) {
            return new int[] { 8 };
        } else if (position == 21 || position == 84) {
            return new int[] { -8 };
        } else {
            return new int[] { -10 };
        }
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int nextCoord : NEXT) {
            int potentialTile = this.position + nextCoord;
            if (Utils.isValid(potentialTile)) {
                if (excludeFirst(this.position, nextCoord) ||
                        excludeSecond(this.position, nextCoord) ||
                        excludeTop(this.position, nextCoord) ||
                        excludeTopBlack(this.position, potentialTile)) {
                    continue;
                }
                final Tile candidateTile = board.getTile(potentialTile);

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
    public Advisor movePiece(Move move) {
        return new Advisor(move.getDestCoord(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return TypePiece.ADVISOR.toString();
    }

    public static boolean excludeFirst(final int position, final int candidateOffset) {
        return Utils.FOURTH[position] && ((candidateOffset == -10) || (candidateOffset == -1)
                || (candidateOffset == 8));
    }

    public static boolean excludeSecond(final int position, final int candidateOffset) {
        return Utils.SIXTH[position] && ((candidateOffset == 10) || (candidateOffset == 1)
                || (candidateOffset == -8));
    }

    public static boolean excludeTop(final int position, final int candidateOffset) {
        return Utils.SEVENTH_RANK[position] && ((candidateOffset == 8) || (candidateOffset == 9)
                || (candidateOffset == 10));
    }

    public static boolean excludeTopBlack(final int position, final int candidateOffset) {
        return Utils.THIRD_RANK[position] && ((candidateOffset == 8) || (candidateOffset == 9)
                || (candidateOffset == 10));
    }
}
