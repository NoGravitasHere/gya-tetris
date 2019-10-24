package Coordinator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import Tetris.GameView;
import Tetris.MyGameModel;
import ga.GeneticAlgorithm;
import ga.GeneticAlgorithmView;
import ga.Genome;

/**
 * @author Pontus Soderlund
 */
public final class Coordinator extends JFrame implements ActionListener {

    //***********************
    // Variables
    //***********************
    public static double MUTATION_RATE = 0.05;
    public static double MUTATION_STEP = 0.1;

    public static final int GENERATION_LIMIT = 10;
    public static final int POPULATION_LIMIT = 10;
    public static final int MOVE_LIMIT = 1000;
    public static final int FALL_FREQUENZY = 150;
    public static final int DISTRUBUTION_RESOLUTION = 10;
    public static final Dimension STANDARD_DIMENSIONS = new Dimension(300, 600);
    public static final Color BACKROUND_COLOR = new Color(230, 230, 230);
    public static final Color GRID_COLOR = new Color(100, 100, 100);

    private Writer generationWriter;
    private Writer eliteWriter;
    private Timer updateTimer;
    private int viewSpeed;

    private Genome fittest;
    private final GeneticAlgorithm ga;
    private final MyGameModel model;

    private JTextField txfGenomeToBePlayed;
    private JSlider sliSpeed;

    //Panels
    private GameView gameView;
    private GeneticAlgorithmView generationView;
    private JPanel controlPanel;

    //***********************
    // Constructor(s)
    //***********************
    public Coordinator(double mutationRate, double mutationStep,
            boolean createWindow, boolean player) {
        Coordinator.MUTATION_RATE = mutationRate;
        Coordinator.MUTATION_STEP = mutationStep;
     
        model = new MyGameModel(false, 10000);
        ga = new GeneticAlgorithm(model);
        gameView = null;

        if (createWindow) {
            generationView = new GeneticAlgorithmView(ga, STANDARD_DIMENSIONS);
            gameView = new GameView(model, true, STANDARD_DIMENSIONS, STANDARD_DIMENSIONS);
            controlPanel = new JPanel();
            viewSpeed = 10;

            fixLayoutNStuff();
            addKeyBindings();

            pack();
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            requestFocus();
            gameView.setDrawable(false);
        }

        boolean loop;
        loop = false;
        loop = true;

        if (player) {
            updateTimer = new Timer(FALL_FREQUENZY, this);
            updateTimer.start();
            gameView.setDrawable(true);
        } else if (loop) {
            ga.setDelay(0);
            fittest = ga.initalize();
        } else {
            ga.setDelay(viewSpeed);
            //playGenome();
        }
    }

    //***********************
    // Main Methods
    //***********************
    public Genome getFittest() {
        return fittest;
    }

    public void playGenome(double a, double b, double c, double d, double e) {
//        ga.setCurrentGenome(a, b, c, d, e);
//        System.out.println(ga.getCurrentGenome().toString());
//
//        while (ga.getCurrentGenome().getMovesTaken() < MOVE_LIMIT) {
//            int pieceId = ga.getCurrentState().getNextIdAndRemove();
//            if (ga.getCurrentState().getBoard().canGenerateNewPiece(pieceId)) {
//                ga.getCurrentState().getBoard().generatePiece(pieceId);
//            } else {
//                break;
//            }
//            ga.makeNextMove();
//        }
    }

    public void fixLayoutNStuff() {
        GridBagLayout lm = new GridBagLayout();
        this.setLayout(lm);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(gameView, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(generationView, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        this.add(controlPanel, gbc);

        double d = 0.05;
        LineBorder lineBorder = new LineBorder(GRID_COLOR, 1);
        EmptyBorder emptyBorder = new EmptyBorder((int) (d * STANDARD_DIMENSIONS.getWidth()),
                (int) (d * STANDARD_DIMENSIONS.getHeight()), (int) (d * STANDARD_DIMENSIONS.getWidth()),
                (int) (d * STANDARD_DIMENSIONS.getHeight()));
        CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
        gameView.setBorder(lineBorder);
        generationView.setBorder(border);
        controlPanel.setBorder(border);

        generationView.setBackground(BACKROUND_COLOR);
        controlPanel.setBackground(BACKROUND_COLOR);

        addToControlPanel();
    }

    //***********************
    // Support Methods
    //***********************
    /**
     * Appends the relevant data to the relevant files...
     */
    public void print() {
        try {
            generationWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File("Generations.txt"), true), "UTF-8"));
            generationWriter.flush();
            eliteWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File("Elites.txt"), true), "UTF-8"));
            eliteWriter.flush();

            generationWriter.append(ga.getCurrentGeneration().toString());
            eliteWriter.append(ga.getCurrentGeneration().getFittest().toString());
            generationWriter.append("\n");
            eliteWriter.append("\n");

            generationWriter.close();
            eliteWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Calls the movement method in MyGameModel if the timer is running and there has been
     * fewer moves than the move limit.
     *
     * @param input the direction in which to move.
     */
    private void keyInput(String input) {
        if (model.getNumberOfMoves() < MOVE_LIMIT) {
            switch (input) {
                case "rotate":
                case "left":
                case "down":
                case "right":
                    if (updateTimer.isRunning()) {
                        model.movement(input, false);
                    }
                    break;
            }
        } else {
            model.lost();
        }
    }

    /**
     * Toggles the timer on or off
     */
    public void toggleTimer() {
        if (updateTimer.isRunning()) {
            updateTimer.stop();
        } else {
            updateTimer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (model.getNumberOfMoves() > MOVE_LIMIT) {
            model.lost();
        }
        if (model.generatePiece(model.getNextPieceId()) && model.getPieceId() >= 0) {

        } else if (model.fall()) {
            model.clearRows();
        } else {
            model.deselectPiece();
            if (!model.generatePiece(model.getNextIdAndRemove())) {
                model.lost();
            }
        }
    }

    //***********************
    // Adders
    //***********************
    /**
     * Adds componennts to the controlPanel
     */
    public void addToControlPanel() {
        BoxLayout layout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
        controlPanel.setLayout(layout);

        txfGenomeToBePlayed = new JTextField();
        sliSpeed = new JSlider(0, 50);
        addButtons();

        sliSpeed.addChangeListener((ChangeEvent e) -> {
            viewSpeed = sliSpeed.getValue();
            ga.setDelay(viewSpeed);
        });
    }

    /**
     * Adds buttons to the controlPanel
     */
    private void addButtons() {
        addButton(controlPanel, "Start", (evt) -> {

        });
        addButton(controlPanel, "Start GA", (evt) -> {

        });
        addButton(controlPanel, "Toggle Select moves", (evt) -> {
            if (gameView.isDrawAllMoves()) {
                gameView.setDrawAllMoves(false);
            } else {
                gameView.setDrawAllMoves(true);
            }
        });
        addButton(controlPanel, "Toggle Player", (evt) -> {

        });
        addButton(controlPanel, "Toggle View", (evt) -> {
            if (gameView.isDrawable()) {
                gameView.setDrawable(false);
            } else {
                gameView.setDrawable(true);
            }
        });
        addButton(controlPanel, "Print Elite", (evt) -> {

        });
        addButton(controlPanel, "Set Genome", (evt) -> {

        });
    }

    /**
     * Adds a button to the specified component.
     *
     * @param comp the component to add to
     * @param text button text
     * @param lambda the action to trigger when the button is pressed.
     */
    private void addButton(JComponent comp, String text, ActionListener lambda) {
        JButton btn = new JButton(text);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lambda.actionPerformed(e);
            }
        });
        comp.add(btn);
    }

    /**
     * Adds key bindings to the frame
     */
    private void addKeyBindings() {
        //Movement
        addKeyBinding(gameView, KeyEvent.VK_W, "rotate", evt -> {
            keyInput("rotate");
        });
        addKeyBinding(gameView, KeyEvent.VK_A, "moveLeft", evt -> {
            keyInput("left");
        });
        addKeyBinding(gameView, KeyEvent.VK_S, "moveDown", evt -> {
            keyInput("down");
        });
        addKeyBinding(gameView, KeyEvent.VK_D, "moveRight", evt -> {
            keyInput("right");
        });

        //Other
        addKeyBinding(gameView, KeyEvent.VK_ESCAPE, "quit", evt -> {
            System.exit(0);
        });
        addKeyBinding(gameView, KeyEvent.VK_L, "lost", evt -> {
            model.lost();
        });
        addKeyBinding(gameView, KeyEvent.VK_P, "pause", evt -> {
            toggleTimer();
        });
        addKeyBinding(gameView, KeyEvent.VK_T, "toggleView", evt -> {
            if (gameView.isDrawable()) {
                gameView.setDrawable(false);
            } else {
                gameView.setDrawable(true);
            }
        });
    }

    /**
     * Adds a key binding to the specifed component.
     *
     * @param comp the component to add to.
     * @param keyCode the key to take action to.
     * @param id the name of the binding.
     * @param lambda the action to trigger.
     */
    private void addKeyBinding(JComponent comp, int keyCode, String id, ActionListener lambda) {
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap ap = comp.getActionMap();

        im.put(KeyStroke.getKeyStroke(keyCode, 0, false), id);
        ap.put(id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lambda.actionPerformed(e);
            }
        });
    }

}
