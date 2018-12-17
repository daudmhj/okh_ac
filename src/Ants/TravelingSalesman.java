package Ants;

import Display.*;
import Graph.*;
import Ants.ExecutionTimer;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * Solves the Traveling Salesman Problem using vanilla Ant Colony Optimization.
 */
public class TravelingSalesman {

    private Graph graph;
    private int numOfAnts, generations;
    private long start;
    private long end;
    
    /**
     * Construct TravelingSalesman.
     * @param ants          the number of ants to run per generation
     * @param generations   the number of generations to run
     * @param evaporation   the rate of evaporation
     * @param alpha         the impact of pheromones on decision making
     * @param beta          the impact of distance in decision making
     */
    public TravelingSalesman (int ants, int generations, double evaporation, int alpha, int beta) {
        this.numOfAnts = ants;
        this.generations = generations;
        graph = IO.Import.getGraph(evaporation, alpha, beta);
    }

    /**
     * Run the algorithm.
     */
    public void run () throws FileNotFoundException {
        // WindowTSP windowTSP = new WindowTSP(graph.getVertices());
        
        ExecutionTimer time = new ExecutionTimer();
        
        Ant bestAnt = null;
        int bestEval = 0;
        
        // delay(1000); // Allow WindowTSP to load.

        for (int i = 0; i < generations; i++) {
            Ant[] ants = createAnts(numOfAnts);
            Ant ant = travel(ants);
            updatePheromones(ants);
            
            if (bestAnt == null) {
                bestAnt = ant;
                bestEval = ant.eval();
            } else if (ant.eval() < bestEval) {
                bestAnt = ant;
                bestEval = ant.eval();
            }
            
            time.end();
            while(time.duration() >= 1000){
                System.out.println("Execution Time: " + time.duration() + " ms");
                System.out.println("Waktu habis! Iterasi terakhir : " + i);
                System.out.print("Best Tour: ");
                System.out.println(bestAnt);
                System.out.println("Evaluation: " + bestEval);
                
                // export to csv
                PrintWriter pw = new PrintWriter(new File("hasilAC.csv"));
                StringBuilder sb = new StringBuilder();
                sb.append("Urutan Kota yang akan dikunjungi");
                sb.append("\n");
                String hasil = String.valueOf(bestAnt);
                String[] urutan = hasil.split(" -> ");
               
                for(int u = 0; u < urutan.length; u++){
                    sb.append(urutan[u]);
                    sb.append("\n");
                }
                
                sb.append("\n");
                sb.append("Distance : " + bestEval);
                
                pw.write(sb.toString());
                pw.close();
                System.out.println("FIle CSV bisa dilihat di root directory project dengan nama hasilAC.csv");
                
                System.exit(0);
            }
            // windowTSP.draw(bestAnt.getTour());
        }
        time.end();
        System.out.println("Execution Time: " + time.duration());
        System.out.println("Iterasi terakhir : " + generations);
        System.out.print("Best Tour: ");
        System.out.println(bestAnt);
        System.out.println("Evaluation: " + bestEval);
        
        // export to csv
        PrintWriter pw = new PrintWriter(new File("hasilAC.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("Urutan Kota yang akan dikunjungi");
        sb.append("\n");
        String hasil = String.valueOf(bestAnt);
        String[] urutan = hasil.split(" -> ");
               
        for(int u = 0; u < urutan.length; u++){
            sb.append(urutan[u]);
            sb.append("\n");
        }
        
        sb.append("\n");
        sb.append("Distance : " + bestEval);
        
        pw.write(sb.toString());
        pw.close();
        System.out.println("FIle CSV bisa dilihat di root directory project dengan nama hasilAC.csv");
        
        System.exit(0);
    }

    /**
     * Create ants and put them on random starting positions on the graph.
     * @param quantity  the quantity of ants to create
     * @return          an array of the ants created
     */
    private Ant[] createAnts (int quantity) {
        Ant[] ants = new Ant[quantity];
        for (int i = 0; i < quantity; i++) {
            ants[i] = new Ant(graph);
        }
        return ants;
    }

    /**
     * Let each ant in the input array travel until an entire tour is completed.
     * @param ants      the ants to allow to travel
     * @return          the ant with the best evaluation
     */
    private Ant travel (Ant[] ants) {

        Ant bestAnt = null;
        int bestEval = 0;

        for (Ant ant : ants) {
            while (ant.notFinished()) {
                ant.travel();
            }

            if (bestAnt == null) {
                bestAnt = ant;
                bestEval = ant.eval();
            } else if (ant.eval() < bestEval) {
                bestAnt = ant;
                bestEval = ant.eval();
            }
        }

        return bestAnt;
    }

    /**
     * Update the pheromones in the graph based on an array of ants with
     * completed tours.
     * @param ants  the ants that will be used to update the pheromones
     */
    private void updatePheromones (Ant[] ants) {
        for (Ant ant : ants) {
            graph.updatePheromone(ant);
        }
    }

    /**
     * Sleep the thread for a specified amount of time.
     * @param ms    milliseconds to sleep for
     */
    private static void delay (int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
