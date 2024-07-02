package Game.Pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Game.Board.Board;
import Game.Board.BoardUtils;
import Game.Board.Move;
import Game.Board.Move.AttackMove;
import Game.Board.Move.MajorMove;
import Game.Board.Tile;
import Game.Side;

public class Bishop extends Piece {
    private final int[] CANDIDATE;

    public Bishop(int position, Side pieceSide) {
        super(PieceType.BISHOP, position, pieceSide);
        this.CANDIDATE = determineCandidateOffsets(this.position);
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
        for (final int nextCoord : CANDIDATE) {
            int candidateDestination = this.position + nextCoord;
            if (BoardUtils.isValid(candidateDestination)) {
                if (isBlocked(board, this.position, nextCoord)) {
                    continue;
                }
                final Tile candidateTile = board.getTile(candidateDestination);

                if (!candidateTile.isOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestination));
                } else {
                    final Piece atDestination = candidateTile.getPiece();
                    final Side destinationSide = atDestination.getSide();

                    if (this.pieceSide != destinationSide) {
                        legalMoves.add(new AttackMove(board, this, candidateDestination, atDestination));
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinates(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
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
