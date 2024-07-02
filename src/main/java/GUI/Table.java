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
import Game.Player.MoveTransition;
import Game.Board.Move;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import Game.Board.BoardUtils;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece playerMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;

    private static Dimension outer = new Dimension(600, 600);
    private static Dimension boarddim = new Dimension(400, 350);
    private static Dimension tiledim = new Dimension(10, 10);
    private static String defaultPiecePath = "art/pieces/chinese/";

    public Table() {
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
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
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoard);

        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("HighLight Legal Moves", false);

        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });

        preferencesMenu.add(legalMoveHighlighterCheckbox);

        return preferencesMenu;
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            java.util.List<TilePanel> traverse(final java.util.List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            java.util.List<TilePanel> traverse(final java.util.List<TilePanel> boardTiles) {
                Collections.reverse(boardTiles);
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract java.util.List<TilePanel> traverse(final java.util.List<TilePanel> boardTiles);

        abstract BoardDirection opposite();
    }

    private class TilePanel extends JPanel {
        private final int tileID;

        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(tiledim);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        playerMovedPiece = null;
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        System.out.println("start");
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            System.out.println(tileID);
                            playerMovedPiece = sourceTile.getPiece();
                            System.out.println(playerMovedPiece.toString());
                            for (Move m : playerMovedPiece.legalMoves(chessBoard)) {
                                System.out.println(m.destination);
                            }
                            ;
                            if (playerMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            System.out.println(tileID);
                            System.out.println(chessBoard);
                            System.out.println(sourceTile.getCoord());
                            System.out.println(destinationTile.getCoord());
                            // System.out.println(chessBoard.redPlayer.legalMoves);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getCoord(),
                                    destinationTile.getCoord());
                            System.out.println("after movefactory");
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            System.out.println("after transition");
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                // add to move logger;
                            }
                            sourceTile = null;
                            destinationTile = null;
                            playerMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
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

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        public void highlightLegalMoves(final Board board) {
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinates() == this.tileID) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/pieces/chinese/BH")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (playerMovedPiece != null && playerMovedPiece.getSide() == board.currentPlayer().getSide()) {
                return playerMovedPiece.legalMoves(board);
            }
            return Collections.emptyList();
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

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
}
