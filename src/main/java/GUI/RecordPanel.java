package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import GUI.Table.MoveLog;
import Game.Board.Board;
import Game.Board.Move;

public class RecordPanel extends JPanel {
    private final DataModel model;
    private final JScrollPane scroller;
    private static final Dimension Dim = new Dimension(200, 500);

    RecordPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scroller = new JScrollPane(table);
        scroller.setColumnHeaderView(table.getTableHeader());
        scroller.setPreferredSize(Dim);
        this.add(scroller, BorderLayout.CENTER);
        this.setVisible(true);
    }

    void redo(final Board board, final MoveLog moveLog) {
        int currentRow = 0;
        this.model.clear();
        for (final Move move : moveLog.getMoves()) {
            final String moveText = move.toString();
            if (move.getMovePiece().getSide().isRed()) {
                this.model.setValueAt(moveText, currentRow, 0);
            } else if (move.getMovePiece().getSide().isBlack()) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        if (moveLog.getMoves().size() > 0) {
            final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1);
            final String moveText = lastMove.toString();

            if (lastMove.getMovePiece().getSide().isRed()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
            } else if (lastMove.getMovePiece().getSide().isBlack()) {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }
        }

        final JScrollBar vertical = scroller.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calculateCheckAndCheckMateHash(final Board board) {
        if (board.currentPlayer().isInCheckMate()) {
            return "#";
        } else if (board.currentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel {
        private final java.util.List<Row> values;
        private static final String[] NAMES = { "RED", "BLACK" };

        public DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null) {
                return 0;
            }
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int column) {
            final Row currentRow = this.values.get(row);
            if (column == 0) {
                return currentRow.getRedMove();
            } else if (column == 1) {
                return currentRow.getBlackMove();
            } else {
                return null;
            }
        }

        @Override
        public void setValueAt(final Object aValue, final int row, final int column) {
            final Row currentRow;
            if (this.values.size() <= row) {
                currentRow = new Row();
                this.values.add(currentRow);
            } else {
                currentRow = this.values.get(row);
            }
            if (column == 0) {
                currentRow.setRedMove((String) aValue);
                fireTableRowsInserted(row, row);
            } else if (column == 1) {
                currentRow.setblackMove((String) aValue);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int column) {
            return NAMES[column];
        }
    }

    private static class Row {
        private String redMove;
        private String blackMove;

        Row() {

        }

        public String getRedMove() {
            return this.redMove;
        }

        public String getBlackMove() {
            return this.blackMove;
        }

        public void setRedMove(final String move) {
            this.redMove = move;
        }

        public void setblackMove(final String move) {
            this.blackMove = move;
        }
    }
}
