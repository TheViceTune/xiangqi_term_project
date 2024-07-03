package Game.Player;

import java.util.Collection;

import Game.Board.Board;
import Game.Board.Move;
import Game.Pieces.Piece;
import Game.Side;

public class RedPlayer extends Player {
    public RedPlayer(final Board board, final Collection<Move> redPLayerLegalMoves,
            final Collection<Move> blackPlayerLegalMoves) {
        super(board, redPLayerLegalMoves, blackPlayerLegalMoves);
    }

    @Override
    public Collection<Piece> getCurPieces() {
        return this.board.getRedPieces();
    }

    @Override
    public Side getSide() {
        return Side.Red;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
}
