/**
 * Generator stolen from open source project
 * <a href="https://github.com/graphstream/gs-algo ">
 * Link to project
 * </a>
 */

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;

import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.degreeDistribution;

/**
 * This program is the main executable
 * and will generate a graph and output
 * the answers needed for my INF246
 * assignment nr 2.
 *
 * @author RakNoel
 * @version 1.0
 * @since 24.05.18
 */
public class main {
    public static void main(String[] args) throws Exception {

        //Setup variables
        final int N = 10;
        final int maxPow = 4;
        final int m = 4;

        //Generator and graph
        Generator gen = new BarabasiAlbertGenerator(m);
        Graph graph = new SingleGraph("Barabàsi-Albert");

        //Connect generator to graph and initialize 2 first nodes
        gen.addSink(graph);
        gen.begin();

        //Create starting graph of m + 1 nodes with degree m
        for (int i = 2; i <= m; i++) {
            Node holder = graph.addNode(getRandomName(20));
            for (Node n : graph.getEachNode())
                graph.addEdge(getRandomName(50), n, holder, false);
        }

        for (int pow = 2; pow <= maxPow; pow++) {
            for (int i = graph.getNodeCount(); i < Math.pow(N, pow); i++) {
                gen.nextEvents();
            }

            int[] degrees = degreeDistribution(graph);
            System.out.printf("Nodes %d: %n", graph.getNodeCount());

            //TODO: Convert to powerLaw and compare
            //Degree distrobution
            System.out.println("Degrees:");
            for (int deg : degrees)
                System.out.printf("%d ", deg);
            System.out.println();

            //Cumulative degree distrobution
            System.out.println("Cumulative degree:");
            for (double deg : cumulativeDistrobution(degrees))
                System.out.printf("%.4f ", deg);
            System.out.println();

            //TODO: Make into function f(N)
            //Avg clustering
            System.out.printf("Avg clustering coefficient %.3f %n%n", averageClusteringCoefficient(graph));
        }
        gen.end();
    }

    /**
     * Method to convert the degree distrobution
     * into a cumulative Degree Distobution
     *
     * @param distro The degree distrobution
     * @return the cumulative degree distrobution based on the param
     */
    private static double[] cumulativeDistrobution(int[] distro) {
        int totalNodes = sum(distro);
        double[] result = new double[distro.length];

        for (int i = 0; i < distro.length; i++) {
            result[i] += (double) distro[i] / (double) totalNodes;
            if (i > 0)
                result[i] += result[i - 1];
        }

        return result;
    }

    /**
     * Method that sums upp a list of integers
     * @param x The list to sum
     * @return the sum of the list
     */
    private static int sum(int[] x) {
        int result = x[0];
        for (int i = 1; i < x.length; i++)
            result += x[i];
        return result;
    }

    /**
     * Random name generator
     * @param length the length of the generated name
     * @return A totaly random ugly af name
     */
    private static String getRandomName(int length) {
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++)
            str.append((char) random.nextInt(93) + 32);

        return str.toString();
    }
}