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

public class Bishop extends Piece {
    private final int[] NEXT;

    public Bishop(int position, Side pieceSide) {
        super(TypePiece.ELEPHENT, position, pieceSide);
        this.NEXT = determineCandidateOffsets(this.position);
    }

    private int[] determineCandidateOffsets(int position) {
        if (position == 2 || position == 47 || position == 6 || position == 51) {
            return new int[] { 16, 20 };
        } else if (position == 18 || position == 63) {
            return new int[] { 20 };
        } else if (position == 38 || position == 83 || position == 42 || position == 87) {
            return new int[] { -16, -20 };
        } else if (position == 22 || position == 67) {
            return new int[] { -20, -16, 16, 20 };
        } else {
            return new int[] { 16 };
        }
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int nextCoord : NEXT) {
            int potentialTile = this.position + nextCoord;
            if (Utils.isValid(potentialTile)) {
                if (isBlocked(board, this.position, nextCoord)) {
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
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestCoord(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return TypePiece.ELEPHENT.toString();
    }

    public static boolean isBlocked(final Board board, final int position, final int candidateOffset) {
        switch (candidateOffset) {
            case -16:
                return board.getTile(position - 8).isOccupied();
            case -20:
                return board.getTile(position - 10).isOccupied();
            case 16:
                return board.getTile(position + 8).isOccupied();
            case 20:
                return board.getTile(position + 10).isOccupied();
            default:
                return false;
        }
    }
}
