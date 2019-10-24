package Coordinator;

import ga.Genome;

/**
 *
 * @author pontus.soderlund
 */
public class GenomePlayer {

    private Genome genome;

    private double rowsCleared;
    private double weightedHeight;
    private double cumulativeHeight;
    private double holes;
    private double bumpiness;

    public GenomePlayer(Genome genome) {
        this.genome = genome;
        setWeights();

    }

    private void setWeights() {
        rowsCleared = genome.getRowsCleared();
        weightedHeight = genome.getWeightedHeight();
        cumulativeHeight = genome.getCumulativeHeight();
        holes = genome.getHoles();
        bumpiness = genome.getBumpiness();
    }
}
