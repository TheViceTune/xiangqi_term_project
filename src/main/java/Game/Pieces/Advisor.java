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

public class Advisor extends Piece {
    private final static int[] CANDIDATE = { -10, -9, -8, -1, 1, 8, 9, 10 };

    public Advisor(final int position, final Side pieceSide) {
        super(PieceType.ADVISOR, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int candidateDestination;

        for (final int nextCoord : CANDIDATE) {
            candidateDestination = this.position + nextCoord;
            if (BoardUtils.isValid(candidateDestination)) {
                if (isFirstColumnKingdomExclusion(this.position, nextCoord) ||
                        isSecondColumnKingdomExclusion(this.position, nextCoord) ||
                        isTopRowKingdomExclusion(position, candidateDestination)) {
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
                    break;
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Advisor movePiece(Move move) {
        return new Advisor(move.getDestinationCoordinates(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return PieceType.ADVISOR.toString();
    }

    public static boolean isFirstColumnKingdomExclusion(final int position, final int candidateOffset) {
        return BoardUtils.FOURTH_COLUMN[position] && ((candidateOffset == -10) || (candidateOffset == 8));
    }

    public static boolean isSecondColumnKingdomExclusion(final int position, final int candidateOffset) {
        return BoardUtils.SIXTH_COLUMN[position] && ((candidateOffset == 10) || (candidateOffset == -8));
    }

    public static boolean isTopRowKingdomExclusion(final int position, final int candidateOffset) {
        return BoardUtils.EIGHTH_RANK[position] && ((candidateOffset == 8) || (candidateOffset == 10));
    }
}
