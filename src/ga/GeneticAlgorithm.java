package ga;

import static Coordinator.Coordinator.GENERATION_LIMIT;
import static Coordinator.Coordinator.MOVE_LIMIT;
import static Coordinator.Coordinator.MUTATION_RATE;
import static Coordinator.Coordinator.MUTATION_STEP;
import static Coordinator.Coordinator.POPULATION_LIMIT;
import Tetris.Board;
import java.beans.*;
import java.util.*;
import Tetris.Model;
import Tetris.MyGameModel;

/**
 * @author Pontus Soderlund
 */
public class GeneticAlgorithm implements Model {

    //***********************
    // Variables
    //***********************
    private PropertyChangeSupport pcs;
    private int currentGenomeId, currentGenerationId, delay;
    private Generation currentGeneration;
    private ArrayList<Generation> previousGenerations;
    private MyGameModel modelState;

    //***********************
    // Contstructor
    //***********************
    public GeneticAlgorithm(MyGameModel state) {
        this.pcs = new PropertyChangeSupport(this);
        this.currentGeneration = new Generation(0);
        this.previousGenerations = new ArrayList<>();
        this.modelState = state;
    }

    //***********************
    // Evolution Methods
    //***********************
    public Genome initalize() {
        for (int i = 0; i < POPULATION_LIMIT; i++) {
            currentGeneration.addGenome(new Genome(i, currentGenerationId));
        }
        while (!stopCriteriaIsFulfilled()) {
            loopThroughGeneration();
            evolve();
        }
        return previousGenerations.get(currentGenerationId - 1).getFittest();
    }

    private void loopThroughGeneration() {
        for (Genome g : currentGeneration.getGenomes()) {
            modelState.reset();

            while (getCurrentGenome().getMovesTaken() < MOVE_LIMIT) {
                int pieceId = getCurrentState().getNextIdAndRemove();
                if (getCurrentState().generatePiece(pieceId)) {
                    makeNextMove();
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                } else {
                    break;
                }
            }

            g.setFitness(modelState.getScore());
            currentGenomeId++;
            pcs.firePropertyChange("nextGenome", currentGenomeId - 1, currentGenomeId);
        }
    }

    /**
     * Creates a new generation by adding the best from the previous generation as well as
     * creating new genomes. When created there is a 75% percent chance that the genome is
     * a combination of random genomes from upper half of the previous generation, a 12.5%
     * chance that the genome is a new one and a 12.5% chance that the genome is a
     * mutation of the fittest genome from thre previous generateion.
     */
    private void evolve() {
        Generation previousGen = currentGeneration.clone();
        Generation modGen = currentGeneration.clone();
        previousGenerations.add(previousGen);

        Genome fittest = currentGeneration.getFittest().clone();
        fittest.setId(fittest.getNewId(0, currentGenerationId + 1));
        fittest.setFitness(-1);
        fittest.setMovesTaken(0);

        currentGenerationId++;
        currentGeneration = new Generation(currentGenerationId);
        currentGeneration.addGenome(fittest);
        currentGenomeId = 0;

        //Remove the worst genomes
        for (int i = 0; i < POPULATION_LIMIT / 2; i++) {
            modGen.removeGenome(i);
        }

        //Creates new Genomes.
        int n = currentGeneration.size() - 1;
        while (currentGeneration.size() < POPULATION_LIMIT) {
            n++;
            if (Math.random() < 0.75) {
                Genome parent1 = modGen.getRandomGenome();
                Genome parent2 = modGen.getRandomGenome();
                currentGeneration.addGenome(makeChild(n, parent1, parent2));
            } else {
                if (Math.random() < 0.5) {
                    currentGeneration.addGenome(new Genome(currentGenerationId, n));
                } else {
                    Genome g = fittest.clone();
                    g.setId(g.getNewId(currentGenerationId, n));
                    currentGeneration.addGenome(mutateGenome(g));
                }
            }
        }

        pcs.firePropertyChange("nextGeneration", previousGen, currentGeneration);
    }

    private Genome mutateGenome(Genome genome) {
        Genome g = genome.clone();
        int n = 2;
        if (Math.random() < MUTATION_RATE) {
            g.setBumpiness(g.getBumpiness() + MUTATION_STEP * (n * Math.random() - 1));
        }
        if (Math.random() < MUTATION_RATE) {
            g.setCumulativeHeight(g.getCumulativeHeight() + MUTATION_STEP * (n * Math.random() - 1));
        }
        if (Math.random() < MUTATION_RATE) {
            g.setHoles(g.getHoles() + MUTATION_STEP * (n * Math.random() - 1));
        }
        if (Math.random() < MUTATION_RATE) {
            g.setRowsCleared(g.getRowsCleared() + MUTATION_STEP * (n * Math.random() - 1));
        }
        if (Math.random() < MUTATION_RATE) {
            g.setWeightedHeight(g.getWeightedHeight() + MUTATION_STEP * (n * Math.random() - 1));
        }
        return g;
    }

    /**
     * Creates a child genome based on two genomes. 
     *
     * @param numberInGeneration the number in the generation.
     * @param g1 One of the parents
     * @param g2 The other parent
     * @return The child that was created and mutated
     */
    private Genome makeChild(int numberInGeneration, Genome g1, Genome g2) {
        return new Genome(numberInGeneration, currentGenerationId, g1, g2);
    }

    private boolean stopCriteriaIsFulfilled() {
        if (currentGenerationId < GENERATION_LIMIT) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * If the number of moves already taken are greater than the move limit the current
     * generation evolves. If not the best move of all moves are selected based on the the
     * move score and then the best one is played.
     */
    private void makeNextMove() {
        ArrayList<Move> possibleMoves = getPossibleMoves();
        Collections.sort(possibleMoves);
        Move nextMove = new Move();
        if (!possibleMoves.isEmpty()) {
            nextMove = possibleMoves.get(0);
        } else {
            return;
        }

        for (int i = 0; i < nextMove.getNumberOfMoves(); i++) {
            switch (nextMove.getMoves().charAt(i)) {
                case 'w':
                    modelState.movement("rotate", false);
                    break;
                case 'a':
                    modelState.movement("left", false);
                    break;
                case 's':
                    modelState.movement("down", false);
                    break;
                case 'd':
                    modelState.movement("right", false);
                    break;
            }
            getCurrentGenome().incrementMovesTaken();
        }
        modelState.deselectPiece();
        modelState.clearRows();
    }

    //***********************
    // Support Methods
    //***********************
    public void loadState(MyGameModel saveState) {
        modelState.setBoard(saveState.getBoard().clone());
        modelState.setScore(saveState.getScore());
        modelState.setNumberOfMoves(saveState.getNumberOfMoves());
    }

    //***********************
    // Setter Methods
    //***********************
    public void setCurrentState(MyGameModel modelState) {
        this.modelState = modelState;
    }

    public void setCurrentGenome(double rc, double wh,
            double th, double h, double b) {
        Genome g = new Genome("none", 0, rc, wh,
                th, h, b, -1, -1);
        currentGeneration.addGenome(g);
        System.out.println(currentGeneration.size());
        currentGenomeId = POPULATION_LIMIT;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    //***********************
    // Getter Methods
    //***********************
    /**
     * Puts all the possible moves and their value (calculated by using the current
     * genomes values) in an ArrayList containing Moves.
     *
     * @return An ArrayList with all possible moves.
     */
    public ArrayList<Move> getPossibleMoves() {
        MyGameModel saveState = getCurrentState().clone();

        ArrayList<Move> moves = new ArrayList<>();

        for (int rotations = 0; rotations < 4; rotations++) {
            for (int i = -5; i <= 5; i++) {
                Move m = new Move("", 0);
                String s = "";
                double moveScore = 0.0;

                loadState(saveState);

                for (int j = 0; j < rotations; j++) {
                    modelState.getBoard().move("rotate");
                    m.move('w');
                }
                if (0 > i) {
                    for (int j = 0; j < Math.abs(i); j++) {
                        if (modelState.getBoard().move("left")) {
                            m.move('a');
                        }
                    }
                } else if (0 < i) {
                    for (int j = 0; j < Math.abs(i); j++) {
                        if (modelState.getBoard().move("right")) {
                            m.move('d');
                        }
                    }
                }

                while (modelState.getBoard().move("down")) {
                    m.move('s');
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                }

                modelState.getBoard().deselectPiece();
                moveScore += getCurrentGenome().getBumpiness() * modelState.getBoard().getBumpiness();
                moveScore += getCurrentGenome().getCumulativeHeight() * modelState.getBoard().getTotalHeight();
                moveScore += getCurrentGenome().getHoles() * modelState.getBoard().getNumberOfHoles();
                moveScore += getCurrentGenome().getRowsCleared() * modelState.getBoard().getNumberOfLinesCleared();
                moveScore += getCurrentGenome().getWeightedHeight() * modelState.getBoard().getHeightOfHighestCollumn();

                m.setMoveScore(moveScore);
                if (m.getNumberOfMoves() + getCurrentGenome().getMovesTaken() < MOVE_LIMIT) {
                    moves.add(m);
                }
            }
        }

        loadState(saveState);
        return moves;
    }

    public MyGameModel getCurrentState() {
        return this.modelState;
    }

    public Genome getCurrentGenome() {
        return currentGeneration.getGenome(currentGenomeId);
    }
 
    public int getCurrentGenerationId() {
        return currentGenerationId;
    }

    public int getCurrentGenomeId() {
        return currentGenomeId;
    }

    public Generation getPreviousGeneration() {
        Collections.sort(previousGenerations);
        return previousGenerations.get(previousGenerations.size() - 1);
    }

    public Generation getCurrentGeneration() {
        return currentGeneration;
    }

    public ArrayList<Generation> getPreviousGenerations() {
        return previousGenerations;
    }

    public int getDelay() {
        return delay;
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
