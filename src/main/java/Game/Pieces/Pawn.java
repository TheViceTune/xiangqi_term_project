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

public class Pawn extends Piece {
    private final static int[] CANDIDATE_BEFORE_RIVER = { 9 };
    private final static int[] CANDIDATE_AFTER_RIVER = { -1, 1, 9 };

    public Pawn(final int position, final Side pieceSide) {
        super(PieceType.PAWN, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int candidateDestination;
        if (BoardUtils.isBeforeRiver(position)) {
            for (final int nextCoord : CANDIDATE_BEFORE_RIVER) {
                candidateDestination = this.position + nextCoord;
                if (BoardUtils.isValid(candidateDestination)) {
                    if (isFirstColumnExclusion(this.position, nextCoord) ||
                            isNinthColumnExclusion(this.position, nextCoord)) {
                        continue;
                    }
                    final Tile candidateTile = Board.getTile(candidateDestination);

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
        } else {
            for (final int nextCoord : CANDIDATE_AFTER_RIVER) {
                candidateDestination = this.position + (this.getSide().getDirection() * nextCoord);
                if (BoardUtils.isValid(candidateDestination)) {
                    if (isFirstColumnExclusion(this.position, nextCoord) ||
                            isNinthColumnExclusion(this.position, nextCoord)) {
                        continue;
                    }
                    final Tile candidateTile = Board.getTile(candidateDestination);

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
        }
        return legalMoves;
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinates(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public static boolean isFirstColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[position] && ((candidateOffset == -1));
    }

    public static boolean isNinthColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.NINTH_COLUMN[position] && ((candidateOffset == 1));
    }
}
