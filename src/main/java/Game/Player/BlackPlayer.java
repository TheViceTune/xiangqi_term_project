package Game.Player;

import java.util.Collection;

import Game.Board.Board;
import Game.Board.Move;
import Game.Pieces.Piece;
import Game.Side;

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final Collection<Move> redPLayerLegalMoves,
            final Collection<Move> blackPlayerLegalMoves) {
        super(board, blackPlayerLegalMoves, redPLayerLegalMoves);
    }

    @Override
    public Collection<Piece> getCurPieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Side getSide() {
        return Side.Black;
    }

    @Override
    public Player getOpponent() {
        return this.board.redPlayer();
    }
}
