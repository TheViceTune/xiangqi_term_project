package GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import Game.Board.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import Game.Board.BoardUtils;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final Board chessBoard;

    private static Dimension outer = new Dimension(600, 600);
    private static Dimension boarddim = new Dimension(400, 350);
    private static Dimension tiledim = new Dimension(10, 10);
    private static String defaultPiecePath = "art/pieces/chinese/";

    public Table() {
        this.gameFrame = new JFrame("Chinese Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(outer);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);

    }

    private JMenuBar populateMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open pgn");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private class TilePanel extends JPanel {
        private final int tileID;

        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(tiledim);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            validate();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileID).isOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(
                            defaultPiecePath + board.getTile(tileID).getPiece().getSide().toString().substring(0, 1)
                                    + board.getTile(this.tileID).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardUtils.TENTH_RANK[this.tileID] || BoardUtils.EIGHTH_RANK[this.tileID]
                    || BoardUtils.SIXTH_RANK[this.tileID] || BoardUtils.FOURTH_RANK[this.tileID]
                    || BoardUtils.SECOND_RANK[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? Color.black : Color.gray);
            } else if (BoardUtils.NINTH_RANK[this.tileID] || BoardUtils.SEVENTH_RANK[this.tileID]
                    || BoardUtils.FIFTH_RANK[this.tileID] || BoardUtils.THIRD_RANK[this.tileID]
                    || BoardUtils.FIRST_RANK[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? Color.black : Color.gray);
            }
        }
    }

    public class BoardPanel extends JPanel {
        final java.util.List<TilePanel> boardTiles;

        public BoardPanel() {
            super(new GridLayout(10, 9));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(boarddim);
            validate();
        }
    }
}
