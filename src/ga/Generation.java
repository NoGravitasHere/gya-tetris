package ga;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pontus Soderlund
 */
public class Generation implements Cloneable, Comparable<Generation> {

    private int generationNumber;
    private int generationFitness;
    private int numberOfIndivuals;
    private int averageFitness;
    private double averageScorePerMove;
    ArrayList<Genome> genomes;

    //***********************
    // Constructors
    //***********************
    public Generation() {
        generationFitness = 0;
        generationNumber = 0;
        numberOfIndivuals = 0;
        genomes = new ArrayList<>();
    }

    public Generation(int generationNumber) {
        this();
        this.generationNumber = generationNumber;
    }

    public Generation(int generationNumber, ArrayList<Genome> genomes) {
        this();
        this.generationNumber = generationNumber;
        this.genomes = genomes;
    }

    private Generation(int gn, int gf, int noi, ArrayList<Genome> g) {
        this.generationNumber = gn;
        this.generationFitness = gf;
        this.numberOfIndivuals = noi;
        this.genomes = g;
    }

    //***********************
    // Getter Methods
    //***********************
    public int getGenerationNumber() {
        return generationNumber;
    }

    public int getGenerationFitness() {
        generationFitness = 0;
        for (int i = 0; i < genomes.size(); i++) {
            generationFitness += genomes.get(i).getFitness();
        }
        return generationFitness;
    }

    public int getAverageFitness() {
        averageFitness = getGenerationFitness() / genomes.size();
        return averageFitness;
    }

    public double getAverageScorePerMove() {
        double spm = 0;
        for (int i = 0; i < genomes.size(); i++) {
            spm += genomes.get(i).getFitnessPerMove();
        }
        averageScorePerMove = spm / ((double) genomes.size());
        return Math.round(averageScorePerMove * 1000.0) / 1000.0;
    }

    public Genome getFittest() {
        Collections.sort(genomes);
        return genomes.get(genomes.size() - 1);
    }

    public Genome getGenome(int index) {
        return genomes.get(index);
    }

    public Genome getRandomGenome() {
        return genomes.get((int) (Math.random() * size()));
    }

    public ArrayList<Genome> getGenomes() {
        return genomes;
    }

    //***********************
    // Setter Methods
    //***********************
    public void setNumberOfIndivuals(int numberOfIndivuals) {
        this.numberOfIndivuals = numberOfIndivuals;
    }

    public void setGenerationFitness(int generationFitness) {
        this.generationFitness = generationFitness;
    }

    public void setGenerationNumber(int generationNumber) {
        this.generationNumber = generationNumber;
    }

    public void setGenomes(ArrayList<Genome> genomes) {
        this.genomes = genomes;
    }

    //***********************
    // Support Methods
    //***********************
    public void removeGenome(int index) {
        genomes.remove(index);
    }

    public void sortAZ() {
        Collections.sort(genomes);
    }

    public int size() {
        return genomes.size();
    }

    public void addGenome(Genome g) {
        genomes.add(g);
        numberOfIndivuals++;
    }

    @Override
    public Generation clone() {

        Generation g = new Generation();
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Generation.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Genome> genomesCopy = new ArrayList<>();
        for (int i = 0; i < genomes.size(); i++) {
            genomesCopy.add(genomes.get(i).clone());
        }

        g.setGenerationFitness(generationFitness);
        g.setGenerationNumber(generationNumber);
        g.setNumberOfIndivuals(numberOfIndivuals);
        g.setGenomes(genomesCopy);

        return g;
    }

    @Override
    public String toString() {
        String s = "";
        s += "Gen: " + getGenerationNumber();
        s += ", Fitness: " + getGenerationFitness();
        s += ", NO Individuals: " + size();
        s += ", Avg. Fitness: " + getAverageFitness();
        return s;
    }

    @Override
    public int compareTo(Generation t) {
        return this.getGenerationNumber() - t.getGenerationNumber();
    }
}
