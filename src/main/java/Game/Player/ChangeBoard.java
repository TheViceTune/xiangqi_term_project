package Game.Player;

import Game.Board.Board;
import Game.Board.Move;

public class ChangeBoard {
    private final Board boardAfterMove;
    private final Move move;
    private final MoveDone moveDone;

    public ChangeBoard(Board transitionalBoard, Move move, MoveDone moveStatus) {
        this.boardAfterMove = transitionalBoard;
        this.move = move;
        this.moveDone = moveStatus;
    }

    public MoveDone getMoveStatus() {
        return this.moveDone;
    }

    public Move getTransitionMove() {
        return this.move;
    }

    public Board getTransitionBoard() {
        return this.boardAfterMove;
    }
}
