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

public class Cannon extends Piece {
    private final static int[] NEXT = { -9, -1, 1, 9 };

    public Cannon(final int position, final Side pieceSide) {
        super(TypePiece.CANNON, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int potentialTile;

        for (final int nextCoord : NEXT) {
            potentialTile = this.position;
            boolean hasJumped = false;

            while (Utils.isValid(potentialTile)) {
                if (excludeFirst(this.position, nextCoord) ||
                        excludeNinth(this.position, nextCoord)) {
                    break;
                }
                potentialTile += nextCoord;
                if (Utils.isValid(potentialTile)) {
                    final Tile candidateTile = Board.getTile(potentialTile);
                    if (!candidateTile.isOccupied()) {
                        if (!hasJumped) {
                            legalMoves.add(new MainMove(board, this, potentialTile));
                        }
                    } else {
                        if (hasJumped) {
                            final Piece atDestination = candidateTile.getPiece();
                            final Side destinationSide = atDestination.getSide();
                            if (this.pieceSide != destinationSide) {
                                legalMoves.add(new AttackMove(board, this, potentialTile, atDestination));
                            }
                            break;
                        }
                        hasJumped = true;
                    }
                }
            }
        }
        return legalMoves;
    }

    @Override
    public Cannon movePiece(Move move) {
        return new Cannon(move.getDestCoord(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return TypePiece.CANNON.toString();
    }

    public static boolean excludeFirst(final int position, final int candidateOffset) {
        return Utils.FIRST[position] && (candidateOffset == -1);
    }

    public static boolean excludeNinth(final int position, final int candidateOffset) {
        return Utils.NINTH[position] && (candidateOffset == 1);
    }
}
