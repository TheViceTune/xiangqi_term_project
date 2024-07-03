package Game.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Game.Board.Board;
import Game.Board.Move;
import Game.Pieces.General;
import Game.Pieces.Piece;
import Game.Side;

public abstract class Player {
    protected final Board board;
    protected final General playerKing;
    public final Collection<Move> legalMoves;
    private final boolean inCheck;

    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.inCheck = !Player.attackOnParticularTile(this.playerKing.getPosition(), opponentMoves).isEmpty();
    }

    private static Collection<Move> attackOnParticularTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestCoord()) {
                attackMoves.add(move);
            }
        }
        return attackMoves;
    }

    private General establishKing() {
        for (final Piece piece : getCurPieces()) {
            if (piece.getType().isKing()) {
                return (General) piece;
            }
        }
        throw new RuntimeException("Not valid board");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return this.inCheck;
    }

    public boolean isInCheckMate() {
        return this.inCheck && !hasRemainingMoves();
    }

    protected boolean hasRemainingMoves() {
        for (final Move move : this.legalMoves) {
            final ChangeBoard transition = makeMove(move);
            if (transition.getMoveStatus().isSuccessful()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInStalemate() {
        return !this.inCheck && !hasRemainingMoves();
    }

    public General getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public ChangeBoard makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            return new ChangeBoard(this.board, move, MoveDone.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.attackOnParticularTile(
                transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) {
            return new ChangeBoard(this.board, move, MoveDone.PLAYER_IN_CHECK);
        }
        return new ChangeBoard(transitionBoard, move, MoveDone.SUCCESS);
    }

    public abstract Collection<Piece> getCurPieces();

    public abstract Side getSide();

    public abstract Player getOpponent();
}
