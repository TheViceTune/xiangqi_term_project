package Game.Board;

import Game.Board.Board.Builder;
import Game.Pieces.Piece;

public abstract class Move {
    final Board board;
    public final Piece movedPiece;
    public final int destination;

    public static final Move NULL_MOVE = new NullMove();

    public Move(final Board board,
            final Piece movedPiece,
            final int destination) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destination;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate()
                && getDestinationCoordinates() == otherMove.getDestinationCoordinates()
                && getMovePiece().equals(otherMove.getMovePiece());
    }

    public Board execute() {
        final Builder builder = new Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setTurn(this.board.currentPlayer().getOpponent().getSide());
        return builder.build();
    }

    public static final class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destination) {
            super(board, movedPiece, destination);
        }
    }

    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(Board board, Piece movedPiece, int destination, Piece attackedPiece) {
            super(board, movedPiece, destination);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

    }

    public static final class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destination) {
            super(board, movedPiece, destination);
        }
    }

    public static final class CannonAttackMove extends AttackMove {

        public CannonAttackMove(Board board, Piece movedPiece, int destination, Piece attackedPiece) {
            super(board, movedPiece, destination, attackedPiece);
        }

    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Not instantiable");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("not instantiable");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate
                        && move.getDestinationCoordinates() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

    public int getDestinationCoordinates() {
        return this.destination;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPosition();
    }

    public Piece getMovePiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

}
