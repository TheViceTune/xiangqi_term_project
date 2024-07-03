package GUI;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

import javax.imageio.*;

import java.util.*;
import java.awt.image.*;
import java.io.*;

import Game.Board.Board;
import Game.Board.Tile;
import Game.Pieces.Piece;
import Game.Player.ChangeBoard;
import Game.Board.Move;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import Game.Board.Utils;

public class Table {
    private final JFrame gameFrame;
    private final Boards boardDisplay;
    private final RecordPanel recordPanel;
    // private final TakenPieces takenPieces;
    private Board chessBoard;
    private Tile startTile;
    private Tile endTile;
    private Piece playerMovedPiece;
    private BoardDirection boardDirection;
    private final MoveLog moveLog;

    private static Dimension outer = new Dimension(1000, 700);
    private static Dimension boarddim = new Dimension(800, 700);
    private static Dimension tiledim = new Dimension(10, 10);
    private static String defaultPiecePath = "art/pieces/chinese/";

    public Table() {
        this.boardDirection = BoardDirection.NORMAL;
        this.moveLog = new MoveLog();
        this.recordPanel = new RecordPanel();
        // this.takenPieces = new TakenPieces();
        this.gameFrame = new JFrame("Chinese Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(outer);
        this.chessBoard = Board.createStandardBoard();
        this.boardDisplay = new Boards();
        // this.gameFrame.add(this.takenPieces, BorderLayout.WEST);
        this.gameFrame.add(this.recordPanel, BorderLayout.EAST);
        this.gameFrame.add(this.boardDisplay, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);

    }

    private JMenuBar populateMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
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

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoard = new JMenuItem("Flip Board");
        flipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardDisplay.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoard);

        preferencesMenu.addSeparator();

        return preferencesMenu;
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            java.util.List<Tiles> traverse(final java.util.List<Tiles> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            java.util.List<Tiles> traverse(final java.util.List<Tiles> boardTiles) {
                Collections.reverse(boardTiles);
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract java.util.List<Tiles> traverse(final java.util.List<Tiles> boardTiles);

        abstract BoardDirection opposite();
    }

    private class Tiles extends JPanel {
        private final int tileID;

        Tiles(final Boards boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(tiledim);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        startTile = null;
                        endTile = null;
                        playerMovedPiece = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        System.out.println("start");
                        if (startTile == null) {
                            startTile = chessBoard.getTile(tileID);
                            System.out.println(tileID);
                            playerMovedPiece = startTile.getPiece();
                            System.out.println(playerMovedPiece.toString());
                            for (Move m : playerMovedPiece.legalMoves(chessBoard)) {
                                System.out.println(m.destination);
                            }
                            ;
                            if (playerMovedPiece == null) {
                                startTile = null;
                            }
                        } else {
                            endTile = chessBoard.getTile(tileID);
                            System.out.println(tileID);
                            System.out.println(chessBoard);
                            System.out.println(startTile.getCoord());
                            System.out.println(endTile.getCoord());
                            // System.out.println(chessBoard.redPlayer.legalMoves);
                            final Move move = Move.MoveGenerator.generateMove(chessBoard, startTile.getCoord(),
                                    endTile.getCoord());
                            final ChangeBoard transition = chessBoard.currentPlayer().makeMove(move);
                            System.out.println("after transition");
                            if (transition.getMoveStatus().isSuccessful()) {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            startTile = null;
                            endTile = null;
                            playerMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                recordPanel.redo(chessBoard, moveLog);
                                // takenPieces.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }

            });
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
            if (Utils.TENTH_RANK[this.tileID] || Utils.EIGHTH_RANK[this.tileID]
                    || Utils.SIXTH_RANK[this.tileID] || Utils.FOURTH_RANK[this.tileID]
                    || Utils.SECOND_RANK[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? Color.black : Color.gray);
            } else if (Utils.NINTH_RANK[this.tileID] || Utils.SEVENTH_RANK[this.tileID]
                    || Utils.FIFTH_RANK[this.tileID] || Utils.THIRD_RANK[this.tileID]
                    || Utils.FIRST_RANK[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? Color.black : Color.gray);
            }
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }
    }

    public class Boards extends JPanel {
        final java.util.List<Tiles> boardTiles;

        public Boards() {
            super(new GridLayout(10, 9));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < Utils.numberOfTiles; i++) {
                final Tiles tilePanel = new Tiles(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(boarddim);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final Tiles tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog {
        private final java.util.List<Move> moves;

        public MoveLog() {
            this.moves = new ArrayList<>();
        }

        public java.util.List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }
    }
}
