package Game.Pieces;

import java.util.Collection;

import Game.Board.Board;
import Game.Board.Move;
import Game.Side;

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int position;
    protected final Side pieceSide;
    protected final boolean isFirstMove;
    private final int hashCode;

    public Piece(final PieceType pieceType, final int position, final Side pieceSide) {
        this.pieceType = pieceType;
        this.position = position;
        this.pieceSide = pieceSide;
        this.isFirstMove = false;
        this.hashCode = computeHashCode();
    }

    @Override
    public boolean equals(final Object other) {

        if (this == other) {
            return true;
        }

        if (!(other instanceof Piece)) {
            return false;
        }

        final Piece otherPiece = (Piece) other;
        return this.position == otherPiece.getPosition() && pieceType == otherPiece.getPieceType()
                && this.pieceSide == otherPiece.getSide() && this.isFirstMove == otherPiece.isFirstMove;
    }

    public int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceSide.hashCode();
        result = 31 * result + position;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public Side getSide() {
        return this.pieceSide;
    }

    public int getPosition() {
        return this.position;
    }

    public abstract Collection<Move> legalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public enum PieceType {

        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        HORSE("H") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("E") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        },
        CANNON("C") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ADVISOR("A") {
            @Override
            public boolean isKing() {
                return false;
            }
        };

        private final String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
    }
}