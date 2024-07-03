package Game.Pieces;

import java.util.Collection;

import Game.Board.Board;
import Game.Board.Move;
import Game.Side;

public abstract class Piece {

    protected final TypePiece type;
    protected final int position;
    protected final Side pieceSide;
    protected final boolean isFirstMove;
    private final int hashCode;

    public Piece(final TypePiece pieceType, final int position, final Side pieceSide) {
        this.type = pieceType;
        this.position = position;
        this.pieceSide = pieceSide;
        this.isFirstMove = true;
        this.hashCode = getHash();
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
        return this.position == otherPiece.getPosition() && type == otherPiece.getType()
                && this.pieceSide == otherPiece.getSide() && this.isFirstMove == otherPiece.isFirstMove;
    }

    public int getHash() {
        int result = type.hashCode();
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

    public int getValue() {
        return this.type.getPieceValue();
    };

    public abstract Piece movePiece(Move move);

    public TypePiece getType() {
        return this.type;
    }

    public enum TypePiece {

        PAWN("P", 1) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        HORSE("H", 4) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ELEPHENT("E", 3) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        GENERAL("G", 100) {
            @Override
            public boolean isKing() {
                return true;
            }
        },
        CANNON("C", 5) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R", 5) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ADVISOR("A", 2) {
            @Override
            public boolean isKing() {
                return false;
            }
        };

        private final String pieceName;
        private int pieceValue;

        TypePiece(final String pieceName, final int pieceValue) {
            this.pieceValue = pieceValue;
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();

        public int getPieceValue() {
            return this.pieceValue;
        }
    }
}