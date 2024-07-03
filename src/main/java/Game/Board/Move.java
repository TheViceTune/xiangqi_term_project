package Game.Board;

import Game.Board.Board.BoardBuilder;
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
        return getCurCoord() == otherMove.getCurCoord()
                && getDestCoord() == otherMove.getDestCoord()
                && getMovePiece().equals(otherMove.getMovePiece());
    }

    @Override
    public String toString() {
        return movedPiece.getType().toString() + Utils.getPositionAtCoord(this.destination);
    }

    public Board execute() {
        final BoardBuilder builder = new BoardBuilder();

        for (final Piece piece : this.board.currentPlayer().getCurPieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.pieceSet(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getCurPieces()) {
            builder.pieceSet(piece);
        }

        builder.pieceSet(this.movedPiece.movePiece(this));
        builder.setTurn(this.board.currentPlayer().getOpponent().getSide());
        return builder.build();
    }

    public static final class MainMove extends Move {
        public MainMove(Board board, Piece movedPiece, int destination) {
            super(board, movedPiece, destination);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MainMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getType().toString() + Utils.getPositionAtCoord(this.destination);
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
            return super.equals(otherAttackMove) && getAttacked().equals(otherAttackMove.getAttacked());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttacked() {
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

    public static class MoveGenerator {
        private MoveGenerator() {
            throw new RuntimeException("x");
        }

        public static Move generateMove(final Board board, final int curCoord,
                final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurCoord() == curCoord
                        && move.getDestCoord() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

    public int getDestCoord() {
        return this.destination;
    }

    public int getCurCoord() {
        return this.movedPiece.getPosition();
    }

    public Piece getMovePiece() {
        return this.movedPiece;
    }

    public boolean isAttack() {
        return false;
    }

    public Piece getAttacked() {
        return null;
    }

}
