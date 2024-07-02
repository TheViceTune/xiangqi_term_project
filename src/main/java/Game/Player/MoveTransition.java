package Game.Player;

import Game.Board.Board;
import Game.Board.Move;

public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(Board transitionalBoard, Move move, MoveStatus moveStatus) {
        this.transitionBoard = transitionalBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Move getTransitionMove() {
        return this.move;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
