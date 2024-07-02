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

public class Cannon extends Piece {
    private final static int[] CANDIDATE = { -9, -1, 1, 9 };

    public Cannon(final int position, final Side pieceSide) {
        super(PieceType.CANNON, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int candidateDestination;

        for (final int nextCoord : CANDIDATE) {
            candidateDestination = this.position;
            while (BoardUtils.isValid(candidateDestination)) {
                if (isFirstColumnExclusion(this.position, nextCoord) ||
                        isNinthColumnExclusion(this.position, nextCoord)) {
                    break;
                }
                candidateDestination += nextCoord;
                if (BoardUtils.isValid(candidateDestination)) {
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
        }
        return legalMoves;
    }

    @Override
    public Cannon movePiece(Move move) {
        return new Cannon(move.getDestinationCoordinates(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return PieceType.CANNON.toString();
    }

    public static boolean isFirstColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[position] && ((candidateOffset == -1));
    }

    public static boolean isNinthColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.NINTH_COLUMN[position] && ((candidateOffset == 1));
    }
}
