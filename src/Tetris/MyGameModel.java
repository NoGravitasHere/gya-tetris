package Tetris;

import Coordinator.Coordinator;
import java.beans.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.LinkedList;
import javax.swing.*;

/**
 * @author Pontus Soderlund
 */
public class MyGameModel implements Model, Cloneable {

    //***********************
    // Variables
    //***********************
    private LinkedList<Integer> numberList;
    private final PropertyChangeSupport pcs;
    private Board board;

    private int score;
    private int numberOfMoves;
    private int nextPieceId;

    private int numberOfPieces;
    private boolean randomNumbers;
    private static boolean generateRandomNumber;

    //***********************
    // Constructor(s)
    //***********************
    /**
     * Creates a new MyGameModel with Board if true else without Board.
     *
     * @param randomNumbers
     * @param numberOfPieces
     * @param b with or without board
     */
    public MyGameModel(boolean randomNumbers, int numberOfPieces) {
        this.pcs = new PropertyChangeSupport(this);
        this.numberList = new LinkedList<>();
        this.numberOfPieces = numberOfPieces;
        this.randomNumbers = randomNumbers;
        this.board = new Board();
        generateNumbers();
    }

    /**
     * Creates a board where all the parameters have to be defined.
     *
     * @param pcs
     * @param board
     * @param score
     * @param numberOfMoves
     */
    public MyGameModel(PropertyChangeSupport pcs, Board board, int score, int numberOfMoves) {
        this.pcs = pcs;
        this.board = board;
        this.score = score;
        this.numberOfMoves = numberOfMoves;
        generateNumbers();
    }

    //***********************
    // Board Methods
    //***********************
    /**
     * Moves or rotates the current piece if possible and fires a a PropertyChangeEvent if
     * the "thinking" variable is false.
     *
     * @param direction Which direction to move/rotate
     * @param thinking Whether or not the Genetic Algorithm is making all possible moves
     */
    public void movement(String direction, boolean thinking) {
        int[][] old = board.getBoard().clone();

        if (board.move(direction)) {
            numberOfMoves++;
            if (direction.equals("down")) {
                score++;
            }
        }
        if (!thinking) {
            pcs.firePropertyChange("movement", old, board.getBoard());
        } else {
            pcs.firePropertyChange("thinkingMovement", old, board.getBoard());
        }
    }

    /**
     * Moves all currently selected tiles down one step and increases the score by one.
     * Fires a PropertyChangeEvent.
     *
     * @return false if it cant fall, else true.
     */
    public boolean fall() {
        if (board.canMove("down")) {
            int[][] old = board.getBoard().clone();
            board.move("down");
            score++;
            numberOfMoves++;
            pcs.firePropertyChange("fall", old, board.getBoard());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears all the rows that are clearable and increases the score by the appropiate
     * amount (depending of number of rows cleared). Fires a ProprtyChangeEvent if
     * drawable is true.
     */
    public void clearRows() {
        MyGameModel old = this.clone();
        switch (board.clearRows()) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
            case 3:
                score += 600;
                break;
            case 4:
                score += 1200;
                break;
        }
        pcs.firePropertyChange("clearRows", old, this);
    }

    public int getPieceId() {
        return board.getPieceId();
    }

    public boolean generatePiece(int pieceId) {
        return board.generatePiece(pieceId);
    }
    
    public void deselectPiece(){
        board.deselectPiece();
    }
    
    //***********************
    // Support Methods
    //***********************
    /**
     * Fills the numberList with numbers from the pseudoRandomNumbers.txt file or random
     * numbers.
     */
    private void generateNumbers() {
        numberList = new LinkedList<>();
        if (!randomNumbers) {
            try {
                Scanner numberReader = new Scanner(new File("pseudoRandomNumbers.txt"));
                for (int i = 0; i < numberOfPieces; i++) {
                    numberList.add(numberReader.nextInt());
                }
            } catch (FileNotFoundException e) {
                System.out.println("404: File Not Found");
            }
        } else {
            for (int i = 0; i < numberOfPieces; i++) {
                numberList.add((int) (1 + Math.random() * 7));
            }
        }
    }

    /**
     * The player loses and the relevant data (name, score, speed, date and number of
     * moves) is appended to the Scores.txt file.
     */
    public void lost() {
        JPanel frame = new JPanel();
        JLabel lblLost = new JLabel("You Lost! Your score is: " + score);
        frame.add(lblLost);

        String name = JOptionPane.showInputDialog(frame);
        String date = LocalDateTime.now().toString();

        try {
            try (Writer w = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File("Scores.txt"), true), "UTF-8"))) {
                w.append(System.lineSeparator() + date + " \t Score: "
                        + score + "\t Speed: " + Coordinator.FALL_FREQUENZY
                        + "\t Name: " + name + "\t Moves: " + numberOfMoves);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        System.exit(score);
    }

    /**
     * Resets the current MyGameModel. Fires a PropertyChangeEvent if drawable is true.
     */
    public void reset() {
        MyGameModel old = this.clone();
        board = new Board();
        score = 0;
        numberOfMoves = 0;
        generateNumbers();

        pcs.firePropertyChange("reset", old, this);
    }

    /**
     * Clones the current MyGameModel and returns an immutable version.
     *
     * @return and immutable copy of this MyGameModel.
     */
    @Override
    public MyGameModel clone() {
        Board boa = board.clone();
        Integer sco = ((Integer) score);
        Integer nom = ((Integer) numberOfMoves);

        MyGameModel m;
        m = new MyGameModel(pcs, boa, sco, nom);
        return m;
    }

    //***********************
    // Getter Methods
    //***********************
    public Board getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    /**
     * Returns the element at index 0 of the numberList.
     *
     * @return the index of the piece id.
     */
    public int getNextPieceId() {
        return numberList.peek();
    }

    /**
     * Returns the element at index 0 of the numberList and removes it.
     *
     * @return the index of the piece id.
     */
    public int getNextIdAndRemove() {
        return numberList.pop();
    }

    //***********************
    // Setter Methods
    //***********************
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public void setGenerateRandomNumber(boolean generateRandomNumber) {
        this.generateRandomNumber = generateRandomNumber;
    }

    //***********************
    // Property Support Methods
    //***********************
    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(propertyName, l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(propertyName, l);
    }

}
