package ga;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pontus Soderlund
 */
public final class Genome implements Comparable<Genome>, Cloneable {

    //***********************
    // Variables
    //***********************
    private String id;
    private int movesTaken, generation, fitness;
    private double rowsCleared;         //The number of rows cleared
    private double weightedHeight;      //The height of the highest collumn
    private double cumulativeHeight;    //The sum of all heights
    private double holes;               //The number of holes
    private double bumpiness;           //The bumpiness of the board
    private double fitnessPerMove;

    //***********************
    // Constructor(s)
    //***********************
    public Genome(int numberInGeneration, int generation) {
        this.id = getNewId(generation, numberInGeneration);
        this.generation = generation;
        this.rowsCleared = Math.random();
        this.weightedHeight = Math.random();
        this.cumulativeHeight = Math.random();
        this.holes = Math.random();
        this.bumpiness = Math.random();
        this.fitness = 0;
    }

    public Genome(int numberInGeneration, int generation, Genome g1, Genome g2) {
        this.id = getNewId(generation, numberInGeneration);
        this.generation = generation;
        if (Math.random() < 0.5) {
            this.rowsCleared = g1.getRowsCleared();
        } else {
            this.rowsCleared = g2.getRowsCleared();
        }
        if (Math.random() < 0.5) {
            this.weightedHeight = g1.getWeightedHeight();
        } else {
            this.weightedHeight = g2.getWeightedHeight();
        }
        if (Math.random() < 0.5) {
            this.cumulativeHeight = g1.getCumulativeHeight();
        } else {
            this.cumulativeHeight = g2.getCumulativeHeight();
        }
        if (Math.random() < 0.5) {
            this.holes = g1.getHoles();
        } else {
            this.holes = g2.getHoles();
        }
        if (Math.random() < 0.5) {
            this.bumpiness = g1.getBumpiness();
        } else {
            this.bumpiness = g2.getBumpiness();
        }
        this.fitness = 0;
    }

    /**
     *
     * @param id the id of the genome
     * @param gen the generation
     * @param rc weight for number of rowsCleared
     * @param wh weight for the weightedHeight
     * @param ch weight for cumulativeHeight
     * @param h weight for holes
     * @param b weight for holes
     * @param f the fitness
     * @param mt number of moves taken
     */
    public Genome(String id, int gen, double rc, double wh,
            double ch, double h, double b, int f, int mt) {
        this.id = id;
        this.generation = gen;
        this.rowsCleared = rc;
        this.weightedHeight = wh;
        this.cumulativeHeight = ch;
        this.holes = h;
        this.bumpiness = b;
        this.fitness = f;
        this.movesTaken = mt;
    }

    //***********************
    // Support Methods
    //***********************
    public void incrementMovesTaken() {
        this.movesTaken++;
    }

    @Override
    public int compareTo(Genome t) {
        return (int) (this.getFitness() - t.getFitness() + 0.5);
    }

    @Override
    public Genome clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Genome.class.getName()).log(Level.SEVERE, null, ex);
        }
        Genome g = new Genome(id, generation, rowsCleared, weightedHeight,
                cumulativeHeight, holes, bumpiness, fitness, movesTaken);
        return g;
    }

    @Override
    public String toString() {
        String s = "";
        s += "ID: " + id;
        s += ", Gen: " + generation;
        s += ", Moves Taken: " + movesTaken;
        s += ", Fitness: " + fitness + " ";
        s += "\n" + rowsCleared;
        s += ", " + weightedHeight;
        s += ", " + cumulativeHeight;
        s += ", " + holes;
        s += ", " + bumpiness;
        return s;
    }

    //***********************
    // Getter Methods
    //***********************
    public String getNewId(int generation, int numberInGeneration) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7 - String.valueOf(generation).length(); i++) {
            sb.append(0);
        }
        sb.append(generation);
        for (int i = 0; i < 3 - String.valueOf(numberInGeneration).length(); i++) {
            sb.append(0);
        }
        sb.append(numberInGeneration);
        return sb.toString();
    }

    public int getFitness() {
        return fitness;
    }

    public int getMovesTaken() {
        return movesTaken;
    }

    public String getId() {
        return id;
    }

    public double getFitnessPerMove() {
        return Math.round((double) fitness / (double) movesTaken * 1000.0) / 1000.0;
    }

    public double getRowsCleared() {
        return rowsCleared;
    }

    public double getWeightedHeight() {
        return weightedHeight;
    }

    public double getCumulativeHeight() {
        return cumulativeHeight;
    }

    public double getHoles() {
        return holes;
    }

    public double getBumpiness() {
        return bumpiness;
    }

    //***********************
    // Setter Methods
    //***********************
    public void setMovesTaken(int movesTaken) {
        this.movesTaken = movesTaken;
    }

    public void setRowsCleared(double rowsCleared) {
        this.rowsCleared = rowsCleared;
    }

    public void setWeightedHeight(double weightedHeight) {
        this.weightedHeight = weightedHeight;
    }

    public void setCumulativeHeight(double cumulativeHeight) {
        this.cumulativeHeight = cumulativeHeight;
    }

    public void setHoles(double holes) {
        this.holes = holes;
    }

    public void setBumpiness(double bumpiness) {
        this.bumpiness = bumpiness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public void setId(String id) {
        this.id = id;
    }
}
