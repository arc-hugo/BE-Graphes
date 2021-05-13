package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OptimalWithOracleTest extends AlgorithmTest {
    /**
     * Test valid path from the custom graph with the three algorithm and compare Dijkstra and A* to Bellman-Ford
     */
    @Test
    public void testValid() {
        // Accessible path from origin to destination
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution bellmanFordSolution = testValidAtoE(new BellmanFordAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testValidAtoE(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testValidAtoE(new AStarAlgorithm(data));

        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), dijkstraSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), dijkstraSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), aStarSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);
    }

    /**
     * Test shortest path from paths folder in the INSA map with the three algorithm and compare Dijkstra and A* to Bellman-Ford
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

        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), dijkstraSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), dijkstraSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), aStarSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);
    }

    /**
     * Test fastest path from paths folder in the INSA map with the three algorithm and compare Dijkstra and A* to Bellman-Ford
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

        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), dijkstraSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), dijkstraSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), aStarSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);
    }

    /**
     * Test shortest path from paths folder in the Haute Garonne map with the three algorithm and compare Dijkstra and A* to Bellman-Ford
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

        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), dijkstraSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), dijkstraSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), aStarSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);
    }

    /**
     * Test fastest path from paths folder in the Haute Garonne map with the three algorithm and compare Dijkstra and A* to Bellman-Ford
     * @throws IOException if paths are not founds or invalids
     */
    @Test
    public void testPFPapeeteFare() throws IOException {
        int insa = 10991;
        int airport = 89149;

        // Fastest path INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution bellmanFordSolution = testINSAAeroportTime(new BellmanFordAlgorithm(data));
        ShortestPathSolution dijkstraSolution = testINSAAeroportTime(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSAAeroportTime(new AStarAlgorithm(data));

        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), dijkstraSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), dijkstraSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), aStarSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);
    }

    /**
     * Test fastest path from paths folder in the Haute Garonne map with the three algorithm and compare Dijkstra and A* to Bellman-Ford
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

        // Compare Dijkstra to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), dijkstraSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), dijkstraSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);

        // Compare A* to Bellman-Ford
        assertEquals(bellmanFordSolution.getStatus(), aStarSolution.getStatus());
        assertEquals(bellmanFordSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);
        assertEquals(bellmanFordSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);
    }
}
