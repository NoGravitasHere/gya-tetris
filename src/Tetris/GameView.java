package Tetris;

import Coordinator.Coordinator;
import java.awt.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Pontus Soderlund
 */
public final class GameView extends JPanel implements PropertyChangeListener {

    //***********************
    // Variables
    //***********************
    private MyGameModel gameModel;
    private boolean drawable, drawAllMoves;
    private final DisplayPanel displayPanel;
    private final InfoPanel infoPanel;

    //***********************
    // Constructor(s)
    //***********************
    public GameView(MyGameModel gameModel, boolean drawable,
            Dimension displayDim, Dimension infoDim) {
        Dimension totDim = new Dimension((int) (displayDim.getWidth() + infoDim.getWidth()),
                (int) (displayDim.getHeight()));
        setModel(gameModel);
        setPreferredSize(totDim);
        setMinimumSize(totDim);

        displayPanel = new DisplayPanel(displayDim);
        infoPanel = new InfoPanel(infoDim);
        displayPanel.setBackground(Coordinator.BACKROUND_COLOR);
        infoPanel.setBackground(Coordinator.BACKROUND_COLOR);
        displayPanel.setBorder(new MatteBorder(0, 0, 0, 1, Coordinator.GRID_COLOR));
        double d = 0.05;
        EmptyBorder emptyBorder = new EmptyBorder((int) (d * infoDim.getWidth()),
                (int) (d * infoDim.getHeight()), (int) (d * infoDim.getWidth()),
                (int) (d * infoDim.getHeight()));
        infoPanel.setBorder(emptyBorder);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(displayPanel);
        add(infoPanel);

        this.drawable = drawable;
        this.drawAllMoves = false;
    }

    //***********************
    // Main Metod
    //***********************
    /**
     * Calls the appropiate methods in the inner classes.
     *
     * @param pce The event on which to respond to.
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (!drawable) {
        } else if (pce.getPropertyName().equals("thinkingMovement") && !drawAllMoves) {
        } else {
            displayPanel.repaint();
            infoPanel.update();
        }
    }

    //***********************
    // Getter Methods
    //***********************
    public MyGameModel getModel() {
        return gameModel;
    }

    public boolean isDrawable() {
        return drawable;
    }

    public boolean isDrawAllMoves() {
        return drawAllMoves;
    }

    //***********************
    // Setter Methods
    //***********************
    public void setModel(MyGameModel gameModel) {
        this.gameModel = gameModel;
        if (this.gameModel != null) {
            this.gameModel.addPropertyChangeListener(this);
        }
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public void setDrawAllMoves(boolean drawAllMoves) {
        this.drawAllMoves = drawAllMoves;
    }

    //***********************
    // Inner Classes
    //***********************
    private class DisplayPanel extends JPanel {

        //***********************
        // Variables
        //***********************
        private final int panelWidth;
        private final int panelHeight;

        //***********************
        // Constructor(s)
        //***********************
        public DisplayPanel(Dimension dim) {
            this.panelWidth = (int) dim.getWidth();
            this.panelHeight = (int) dim.getHeight();
            setPreferredSize(dim);
            setMinimumSize(dim);
            setMaximumSize(dim);
        }

        //***********************
        // Drawing Methods
        //***********************
        /**
         * Draws the board, tiles and grid.
         */
        @Override
        protected void paintComponent(Graphics g) {
            if (drawable) {
                super.paintComponent(g);
                drawTiles(g);
                drawGrid(g);
            }
        }

        /**
         * Draws all the Tiles on the board
         */
        private void drawTiles(Graphics g) {
            Board board = gameModel.getBoard();
            for (int i = 0; i < Board.NOCOLLUMNS; i++) {
                for (int j = 0; j < Board.NOROWS; j++) {
                    drawTile(g, i, j, board.getTile(i, j));
                }
            }
        }

        /**
         * Draws a single Tile on the board when called
         *
         * @param x The X position of the Tile
         * @param y The Y position of the Tile
         * @param c If the Tile is the current Tile then true, else false
         */
        private void drawTile(Graphics g, int x, int y, int tileId) {
            Color c = null;
            Color iColor = new Color(54, 224, 255);
            Color jColor = new Color(28, 118, 188);
            Color lColor = new Color(248, 147, 29);
            Color oColor = new Color(254, 227, 86);
            Color sColor = new Color(83, 213, 4);
            Color zColor = new Color(249, 35, 56);
            Color tColor = new Color(201, 115, 255);

            switch (Math.abs(tileId)) {
                case 0:
                    return;
                case 1:
                    c = iColor;
                    break;
                case 2:
                    c = jColor;
                    break;
                case 3:
                    c = lColor;
                    break;
                case 4:
                    c = oColor;
                    break;
                case 5:
                    c = sColor;
                    break;
                case 6:
                    c = zColor;
                    break;
                case 7:
                    c = tColor;
                    break;
            }
            g.setColor(c);

            int a = (int) (((double) panelWidth / (double) Board.NOCOLLUMNS));
            int b = (int) (((double) panelHeight / (double) Board.NOROWS));
            g.fillRect(x * a, y * b, a, b);
        }

        /**
         * Draws the grid.
         */
        private void drawGrid(Graphics g) {
            g.setColor(Coordinator.GRID_COLOR);
            int wMult = (int) ((double) panelWidth / (double) Board.NOCOLLUMNS);
            int hMult = (int) ((double) panelHeight / (double) Board.NOROWS);
            for (int i = 0; i < Board.NOCOLLUMNS; i++) {
                for (int j = 0; j < Board.NOROWS; j++) {
                    g.drawLine(i * wMult, 0, i * wMult, panelHeight);
                    g.drawLine(0, j * hMult, panelWidth, j * hMult);
                }
            }
        }
    }

    private class InfoPanel extends JPanel {

        //***********************
        // Variables
        //***********************
        private JLabel lblScore, lblMoves, lblButtons, lblBoard, lblController;

        //***********************
        // Constructor(s)
        //***********************
        public InfoPanel(Dimension dim) {
            setPreferredSize(dim);
            setMinimumSize(dim);
            setMaximumSize(dim);
            addComponents();
        }

        //***********************
        // Main ethod
        //***********************
        /**
         * Updates the values of the labels lblScore and lblMoves
         */
        public void update() {
            lblScore.setText("Score: " + gameModel.getScore());
            lblMoves.setText("Moves: " + gameModel.getNumberOfMoves() + " / " + Coordinator.MOVE_LIMIT);
        }

        //***********************
        // Support Methods
        //***********************
        /**
         * Adds all the components to the InfoPanel
         */
        private void addComponents() {
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(layout);
            lblScore = new JLabel();
            lblMoves = new JLabel();
            lblButtons = new JLabel();
            lblBoard = new JLabel();
            lblController = new JLabel();

            Font font = new Font(Font.MONOSPACED, Font.PLAIN, 18);
            addLabel(this, lblController, "<html> <h1> Info and Data </h1> <html>", font);
            addLabel(this, lblButtons, "<html> "
                    + "A - Left &nbsp; D - Right <br> "
                    + "S - Down &nbsp; W - Rotate <br> "
                    + "P - Pause &nbsp;L - Lost <br>"
                    + "Escape - Exit &nbsp;"
                    + "<br> &nbsp; </html>", font);
            //addLabel(this, lblBoard, font, "lblBoard");
            addLabel(this, lblMoves, "Moves: 0", font);
            addLabel(this, lblScore, "Score: 0", font);
        }

        /**
         * Adds a label to the specified component where the font and text to be displayed
         * are specified.
         *
         * @param comp The component to add to
         * @param label The label to add
         * @param text The text to display on the panel
         * @param font The font in which to displat the text
         */
        private void addLabel(JComponent comp, JLabel label, String text, Font font) {
            label.setText(text);
            label.setFont(font);
            comp.add(label);
        }

    }
}
