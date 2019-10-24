package ga;

import java.awt.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import Coordinator.Coordinator;
import static Coordinator.Coordinator.GENERATION_LIMIT;
import static Coordinator.Coordinator.MUTATION_STEP;

/**
 * @author Pontus Soderlund
 */
public class GeneticAlgorithmView extends JPanel implements PropertyChangeListener {

    protected GeneticAlgorithm gaModel;

    private JPanel topPanel;
    private BotPanel botPanel;

    private JLabel lblGeneration;
    private JLabel lblGenome;
    private JLabel lblFittest;
    private JLabel lblAverage;
    private JLabel lblMutationRate;
    private JLabel lblMutationStep;
    private JProgressBar prgGeneration;
    private JProgressBar prgGenome;

    public GeneticAlgorithmView(GeneticAlgorithm gaModel, Dimension dim) {
        setModel(gaModel);
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        setPreferredSize(dim);
        Dimension rightDim = new Dimension((int) dim.getWidth(), (int) (dim.getHeight() * (1.0 / 3.0)));
        topPanel = new JPanel();
        botPanel = new BotPanel(rightDim);

        setupLeftPanel();
        setupRightPanel();
        this.setBackground(Coordinator.BACKROUND_COLOR);
        topPanel.setBackground(Coordinator.BACKROUND_COLOR);
        botPanel.setBackground(Coordinator.BACKROUND_COLOR);

        LineBorder border = new LineBorder(Color.GRAY);
        topPanel.setBorder(border);
        botPanel.setBorder(border);
        add(topPanel);
        add(botPanel);
    }

    private void setupRightPanel() {

    }

    private void setupLeftPanel() {
        BoxLayout layout = new BoxLayout(topPanel, BoxLayout.Y_AXIS);
        topPanel.setLayout(layout);
        lblMutationRate = new JLabel("Mutation rate: " + Coordinator.MUTATION_RATE);
        lblMutationStep = new JLabel("Mutation step: " + MUTATION_STEP);
        lblGeneration = new JLabel("Generation: 0 / " + GENERATION_LIMIT);
        lblGenome = new JLabel("Genome: 1 / " + Coordinator.POPULATION_LIMIT);
        lblAverage = new JLabel("<html> Avg Fit: <br>"
                + "Avg Spm:");
        lblFittest = new JLabel("<html> <b>Fittest</b> <br>"
                + "Fittness: " + "<br>"
                + "Moves: " + "<br>"
                + "Spm: ");
        prgGeneration = new JProgressBar(0, GENERATION_LIMIT);
        prgGenome = new JProgressBar(0, Coordinator.POPULATION_LIMIT);

        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 18);
        lblMutationRate.setFont(font);
        lblMutationStep.setFont(font);
        lblGeneration.setFont(font);
        lblGenome.setFont(font);
        lblAverage.setFont(font);
        lblFittest.setFont(font);
        prgGeneration.setFont(font);
        prgGenome.setFont(font);

        topPanel.add(lblMutationRate);
        topPanel.add(lblMutationStep);
        topPanel.add(lblGeneration);
        topPanel.add(prgGeneration);
        topPanel.add(lblGenome);
        topPanel.add(prgGenome);
        topPanel.add(lblAverage);
        topPanel.add(lblFittest);
    }

    private void updateLeftPanel(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals("nextGeneration")) {
            Generation preGen = gaModel.getPreviousGeneration();
            Genome fittest = gaModel.getPreviousGeneration().getFittest();
            //System.out.println(fittest.toString());
            
            prgGeneration.setValue(gaModel.getCurrentGenerationId());
            lblGeneration.setText("Generation: " + gaModel.getCurrentGenerationId() + " / " + GENERATION_LIMIT);
            lblAverage.setText("<html> Avg Fit: " + preGen.getAverageFitness()
                    + "<br> Avg Spm: " + preGen.getAverageScorePerMove());
            lblFittest.setText("<html> <b>Fittest </b> <br>"
                    + "Fitness: " + fittest.getFitness() + "<br>"
                    + "Moves: " + fittest.getMovesTaken() + "<br>"
                    + "Spm: " + fittest.getFitnessPerMove());

        } else if (pce.getPropertyName().equals("nextGenome")) {
            prgGenome.setValue(gaModel.getCurrentGenomeId());
            lblGenome.setText("Genome: " + gaModel.getCurrentGenomeId() + " / " + Coordinator.POPULATION_LIMIT);
        }
    }

    private void setModel(GeneticAlgorithm gaModel) {
        this.gaModel = gaModel;
        if (this.gaModel != null) {
            this.gaModel.addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        updateLeftPanel(pce);
        if (pce.getPropertyName().equals("nextGeneration")) {
            botPanel.updateRightPanel(pce);
        }
    }

    private class BotPanel extends JPanel {

        private Dimension dim;
        private int height;
        private int width;

        //Size equals to the resolution, number of fitness values within the "step" span
        private int[] distrubution;
        //All the genomes fitness
        private int[] fitness;
        //Total number of bars
        private int resolution = Coordinator.DISTRUBUTION_RESOLUTION;
        private int maxFitness;
        private int minFitness;
        //How large each bar should be
        private int fitnessStep;
        private int xStep;
        private int yStep;

        public BotPanel(Dimension dim) {
            this.setPreferredSize(dim);
            this.setMinimumSize(dim);
            height = (int) this.getPreferredSize().getHeight();
            width = (int) this.getPreferredSize().getWidth();
        }

        public void updateRightPanel(PropertyChangeEvent pce) {
            ArrayList<Genome> gen = gaModel.getPreviousGenerations().get(gaModel.getPreviousGenerations().size() - 1).getGenomes();
            Collections.sort(gen);
            fitness = new int[Coordinator.POPULATION_LIMIT];
            distrubution = new int[resolution];
            maxFitness = gen.get(gen.size() - 1).getFitness();
            minFitness = gen.get(0).getFitness();
            fitnessStep = (maxFitness - minFitness) / resolution;
            fitnessStep += 10;

            for (int i = 0; i < fitness.length; i++) {
                fitness[i] = gen.get(i).getFitness();
            }

            for (int i = 0; i < distrubution.length; i++) {
                for (int j = 0; j < fitness.length; j++) {
                    if (fitness[j] < ((fitnessStep) * (i + 1))
                            && fitness[j] >= (fitnessStep * i)
                            && fitness[j] != -1) {

                        distrubution[i]++;
                        fitness[j] = -1;
                    }
                }
            }

            getXStep();
            getYStep();

            botPanel.repaint();
        }

        public void getXStep() {
            width = this.getWidth();
            xStep = width / (resolution + 2);
        }

        public void getYStep() {
            height = this.getHeight();
            int m = 0;
            for (int i = 0; i < distrubution.length; i++) {
                if (distrubution[i] > m) {
                    m = distrubution[i];
                }
            }
            yStep = height / (m + 2);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (xStep != 0 && yStep != 0) {
                int n = (width / xStep);
                g.setColor(Color.black);
                for (int i = 0; i < resolution + 2; i++) {
                    if (i != 0 && i != resolution + 1) {
                        g.fillRect((i * xStep),
                                (height - distrubution[i - 1] * yStep), xStep, height - yStep);
                    }
                }
            }
        }
    }
}
