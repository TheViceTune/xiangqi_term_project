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

public class Pawn extends Piece {
    private final static int[] POTENTIAL_PRERIVER = { 9 };
    private final static int[] POTENTIAL_POSTRIVER = { -1, 1, 9 };

    public Pawn(final int position, final Side pieceSide) {
        super(TypePiece.PAWN, position, pieceSide);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        int potentialTile;
        if (Utils.isBeforeRiver(position, this.pieceSide)) {
            for (final int nextCoord : POTENTIAL_PRERIVER) {
                potentialTile = this.position + (this.pieceSide == Side.Red ? nextCoord : -nextCoord);
                if (Utils.isValid(potentialTile)) {
                    if (excludeFirst(this.position, nextCoord) ||
                            excludeNinth(this.position, nextCoord)) {
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
        } else {
            for (final int nextCoord : POTENTIAL_POSTRIVER) {
                potentialTile = this.position + (this.pieceSide == Side.Red ? nextCoord : -nextCoord);
                if (Utils.isValid(potentialTile)) {
                    if (excludeFirst(this.position, nextCoord) ||
                            excludeNinth(this.position, nextCoord)) {
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
        }
        return legalMoves;
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestCoord(), move.getMovePiece().getSide());
    }

    @Override
    public String toString() {
        return TypePiece.PAWN.toString();
    }

    public static boolean excludeFirst(final int position, final int candidateOffset) {
        return Utils.FIRST[position] && (candidateOffset == -1);
    }

    public static boolean excludeNinth(final int position, final int candidateOffset) {
        return Utils.NINTH[position] && (candidateOffset == 1);
    }
}
