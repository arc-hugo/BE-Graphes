package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class OptimalWithOracleTest extends AlgorithmTest {

    private void testThreeAlgorithm(ShortestPathSolution bellmanFord, ShortestPathSolution dijkstra, ShortestPathSolution aStar) {
        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFord.getStatus(), dijkstra.getStatus());
        assertEquals(bellmanFord.getPath().getOrigin().getId(), dijkstra.getPath().getOrigin().getId());
        assertEquals(bellmanFord.getPath().getDestination().getId(), dijkstra.getPath().getDestination().getId());
        assertEquals(bellmanFord.getPath().getLength(), dijkstra.getPath().getLength(), 0);
        assertEquals(bellmanFord.getPath().getMinimumTravelTime(), dijkstra.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFord.getStatus(), aStar.getStatus());
        assertEquals(bellmanFord.getPath().getOrigin().getId(), aStar.getPath().getOrigin().getId());
        assertEquals(bellmanFord.getPath().getDestination().getId(), aStar.getPath().getDestination().getId());
        assertEquals("o = "+bellmanFord.getPath().getOrigin()+"d = "+bellmanFord.getPath().getDestination()
                ,bellmanFord.getPath().getLength(), aStar.getPath().getLength(), 0);
        assertEquals(bellmanFord.getPath().getMinimumTravelTime(), aStar.getPath().getMinimumTravelTime(), 0);
    }

    /**
     * Test valid path from custom graph with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     */
    @Test
    public void testValid() {
        // Accessible path from origin to destination
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution bellmanFordSolution = testValidAtoE(new BellmanFordAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testValidAtoE(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testValidAtoE(new AStarAlgorithm(data));

        testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);
    }

    /**
     * Test shortest path from paths folder in INSA map with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     * @throws IOException if paths are not founds or invalids
     */
    @Test
    public void testINSARangueilEntree() throws IOException {
        int rangueil = 552;
        int entree = 254;
        // Shortest path from Rangueil to INSA restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(insa, insa.get(rangueil), insa.get(entree), ArcInspectorFactory.getAllFilters().get(1));
        ShortestPathSolution bellmanFordSolution = testRangueilINSA(new DijkstraAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testRangueilINSA(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testRangueilINSA(new DijkstraAlgorithm(data));

        testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);
    }

    /**
     * Test fastest path from paths folder in INSA map with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     * @throws IOException if paths are not founds or invalids
     */
    @Test
    public void testINSARangueilR2() throws IOException {
        int rangueil = 552;
        int r2 = 526;
        // Shortest path from Rangueil to R2 open to any roads
        ShortestPathData data = new ShortestPathData(insa, insa.get(rangueil), insa.get(r2), ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution bellmanFordSolution = testRangueilR2(new BellmanFordAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testRangueilR2(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testRangueilR2(new AStarAlgorithm(data));

        testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);
    }

    /**
     * Test shortest path from paths folder in French Polynesia map with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     * @throws IOException if paths are not founds or invalids
     */
    @Test
    public void testPFPapeetePihau() throws IOException {
        int papeete = 3382;
        int pihau = 13979;

        // Shortest path from Papeete to Pihau on any road
        ShortestPathData data = new ShortestPathData(frenchpolynesia, frenchpolynesia.get(papeete), frenchpolynesia.get(pihau), ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution bellmanFordSolution = testPapeetePihau(new BellmanFordAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testPapeetePihau(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testPapeetePihau(new AStarAlgorithm(data));

        testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);
    }

    /**
     * Test fastest path from paths folder in Haute Garonne map with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     * @throws IOException if paths are not founds or invalids
     */
    @Test
    public void testToulouseINSATonton() throws IOException {
        int insa = 11325;
        int tonton = 18935; // perhaps the most important path

        // Fastest path from INSA to Chez Tonton restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(toulouse, toulouse.get(insa), toulouse.get(tonton), ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution bellmanFordSolution = testINSATonton(new BellmanFordAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testINSATonton(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSATonton(new AStarAlgorithm(data));

        testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);
    }

    /**
     * Test shortest path on 50 random origin/destination in Haute Garonne map with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     */
    @Test
    public void testToulouseRandomShortest() {
        int i = 0;
        Random rand = new Random();
        while (i < 50) {
            // Shortest path from random origin to random destination by any road or car only
            ShortestPathData data = new ShortestPathData(toulouse,
                    toulouse.get(rand.nextInt(toulouse.size())),
                    toulouse.get(rand.nextInt(toulouse.size())),
                    ArcInspectorFactory.getAllFilters().get(rand.nextInt(2)));
            ShortestPathSolution bellmanFordSolution = new BellmanFordAlgorithm(data).run();
            if (bellmanFordSolution.isFeasible()) {
                ShortestPathSolution dijkstraSolution = new DijkstraAlgorithm(data).run();
                ShortestPathSolution aStarSolution = new AStarAlgorithm(data).run();

                testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);

                i++;
            }
        }
    }

    /**
     * Test fastest path on 50 random origin/destination in Haute Garonne map with the three algorithm.
     * Compare Dijkstra and A* to Bellman-Ford which serves as an oracle.
     */
    @Test
    public void testToulouseRandomFastest() {
        int i = 0;
        Random rand = new Random();
        while (i < 50) {
            // Shortest path from random origin to random destination by any road, car only or pedestrian only
            ShortestPathData data = new ShortestPathData(toulouse,
                    toulouse.get(rand.nextInt(toulouse.size())),
                    toulouse.get(rand.nextInt(toulouse.size())),
                    ArcInspectorFactory.getAllFilters().get(rand.nextInt(3)+2));
            ShortestPathSolution bellmanFordSolution = new BellmanFordAlgorithm(data).run();
            if (bellmanFordSolution.isFeasible()) {
                ShortestPathSolution dijkstraSolution = new DijkstraAlgorithm(data).run();
                ShortestPathSolution aStarSolution = new AStarAlgorithm(data).run();

                testThreeAlgorithm(bellmanFordSolution, dijkstraSolution, aStarSolution);

                i++;
            }
        }
    }
}
