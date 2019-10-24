/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coordinator;

import ga.Generation;
import ga.Genome;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author pontus.soderlund
 */
public class Tester {

    int noTestPerCoordinator;

    public Tester() throws IOException {

//        Coordinator c = new Coordinator(0, 0, true, true);
//        
//        Coordinator c = new Coordinator(0.05, 0.2, false, false);
//        System.out.println(c.getFittest());
//        noTestPerCoordinator = 10;
//        test();

        Genome g = null;
//      = new Genome
        playGenome(g);
    }

    private void test() throws IOException {
        double rateStep = 0.1;
        double rateLower = 0;
        double rateUpper = 1;

        double stepStep = 0.1;
        double stepLower = 0;
        double stepUpper = 1;

        int noCoordinators = 0;
        Generation gen = new Generation();
        double t0 = System.currentTimeMillis();

        for (double mutationRate = rateLower; mutationRate < rateUpper + rateStep * 0.9; mutationRate += rateStep) {
            for (double mutationStep = stepLower; mutationStep < stepUpper + stepStep * 0.9; mutationStep += stepStep) {
                Writer writer = new BufferedWriter(new FileWriter(new File("averageBestFitness.csv"), true));
                System.out.println("Rate: " + mutationRate + ", " + "Step: " + mutationStep);
                for (int i = 0; i < noTestPerCoordinator; i++) {
                    Coordinator c = new Coordinator(mutationRate, mutationStep, false, false);
                    gen.addGenome(c.getFittest().clone());
                    noCoordinators++;
                }
                writer.append(String.valueOf(gen.getAverageFitness() + ", "));
                writer.close();
            }
            Writer writer = new BufferedWriter(new FileWriter(new File("averageBestFitness.csv"), true));
            writer.append("\n");
            writer.close();

        }

        double t1 = System.currentTimeMillis();

        System.out.println("No. Coordinators: " + noCoordinators);
        System.out.println("No. Genomes: " + Coordinator.GENERATION_LIMIT * Coordinator.POPULATION_LIMIT);
        System.out.println("Tot. Genomes: " + noCoordinators * Coordinator.GENERATION_LIMIT * Coordinator.POPULATION_LIMIT);
        System.out.println((t1 - t0) / 1000.0 + " seconds");
        System.out.println("Done");

    }

    private void playGenome(Genome g) {

    }

    //26 genomes / second. shit
    //Original: 100*100*10*20*20 = 40,000,000 genomes --> 1,500,000 s --> 430 h
    //Possible 8 h --> 29,000 s --> 750,000 genomes
    //100*100*10*10*10 = 10,000,000
    //50*100*10*36 = 1,800,000
    //50*50*10*30 = 750,000
    //10*50*10*100 = 500,000
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Tester t = new Tester();
    }

}
