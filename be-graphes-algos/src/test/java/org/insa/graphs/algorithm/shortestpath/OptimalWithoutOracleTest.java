package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.junit.Test;

public class OptimalWithoutOracleTest extends AlgorithmTest {

    @Test
    public void testHGINSAAeroportShortest() {
        int insa = 10991;
        int airport = 89149;

        // Shortest path from INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(1));
        ShortestPathSolution dijkstraSolution = testValidAtoE(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testValidAtoE(new AStarAlgorithm(data));
    }

    @Test
    public void testHGINSAAeroportFastest() {
        int insa = 10991;
        int airport = 89149;

        // Fastest path INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution dijkstraSolution = testValidAtoE(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testValidAtoE(new AStarAlgorithm(data));
    }

    @Test
    public void testHGINSABikiniShortest() {
        int insa = 10991;
        int bikini = 63104;

        // Shortest path from INSA to Bikini on any road
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution dijkstraSolution = testValidAtoE(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testValidAtoE(new AStarAlgorithm(data));
    }

    @Test
    public void testHGINSABikiniFastest() {
        int insa = 10991;
        int bikini = 63104;

        // Fastest path from INSA to Bikini restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution dijkstraSolution = testValidAtoE(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testValidAtoE(new AStarAlgorithm(data));
    }
}
