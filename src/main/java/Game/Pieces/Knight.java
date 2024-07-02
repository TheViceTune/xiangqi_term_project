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

public class Knight extends Piece {

    private final static int[] CANDIDATE = { -19, -17, -11, -7, 7, 11, 17, 19 };

    public Knight(final int position, final Side pieceSide) {
        super(PieceType.KNIGHT, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int candidateDestination;

        for (final int nextCoord : CANDIDATE) {
            candidateDestination = this.position + nextCoord;
            if (BoardUtils.isValid(candidateDestination)) {
                if (isFirstColumnExclusion(this.position, nextCoord) ||
                        isSecondColumnExclusion(this.position, nextCoord) ||
                        isEighthColumnExclusion(this.position, nextCoord) ||
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
        return legalMoves;
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinates(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    public static boolean isFirstColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[position] && ((candidateOffset == -19) || (candidateOffset == -11)
                || (candidateOffset == 7) || (candidateOffset == 17));
    }

    public static boolean isSecondColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[position] && ((candidateOffset == 7) || (candidateOffset == -11));
    }

    public static boolean isEighthColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[position] && ((candidateOffset == -7) || (candidateOffset == 11));
    }

    public static boolean isNinthColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.NINTH_COLUMN[position] && ((candidateOffset == 19) || (candidateOffset == 11)
                || (candidateOffset == -7) || (candidateOffset == -17));
    }
}
