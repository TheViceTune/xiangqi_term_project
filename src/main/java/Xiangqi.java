
import GUI.Table;
import Game.Board.Board;

public class Xiangqi {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();

        System.out.println(board);

        Table table = new Table();
    }
}
