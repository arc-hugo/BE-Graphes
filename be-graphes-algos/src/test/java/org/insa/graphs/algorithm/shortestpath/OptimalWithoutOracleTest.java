package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class OptimalWithoutOracleTest extends AlgorithmTest {

    // Supplementary graphs
    Graph midi;

    private void testShortest (ShortestPathData data, ShortestPathSolution dijkstraSolution, ShortestPathSolution aStarSolution) {
        // Proof for optimal Dijkstra :
        // Label cost is the shortest road cost's from the origin to this label.
        // Supplementary proof for optimal AStar :
        // Estimated cost to destination must be inferior or equals to their real cost for each labels.
        float cost = 0;
        Point dest = aStarSolution.getPath().getDestination().getPoint();
        float est;
        List<Arc> list = dijkstraSolution.getPath().getArcs();

        // Same cost for Dijkstra and AStar
        assertEquals(dijkstraSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);

        // For each arc in path, assert if they really are the shortest allowed between their origin and destination
        for (Arc arc : list) {
            float min = Float.POSITIVE_INFINITY;
            Arc road = null;
            for (Arc other : arc.getOrigin().getSuccessors()) {
                if (other.getDestination().getId() == arc.getDestination().getId() &&
                        data.isAllowed(other) &&
                        min > data.getCost(other)) {
                    road = other;
                    min = (float) data.getCost(other);
                }
            }
            // Assert if we found the same arc and cost
            assertEquals(road, arc);
            assertEquals(min, data.getCost(arc), 0);
            // Assert if estimation is inferior or equals to real cost (with a delta for floating comparaison)
            est = (float) arc.getOrigin().getPoint().distanceTo(dest);
            assertTrue(aStarSolution.getPath().getLength()-cost+0.01+" >= "+est,
                    aStarSolution.getPath().getLength()-cost+0.01 >= est);
            // Update cost
            cost += min;
        }
        assertEquals(cost, dijkstraSolution.getPath().getLength(), 0.01);
    }

    private void testFastest (ShortestPathData data, ShortestPathSolution dijkstraSolution, ShortestPathSolution aStarSolution) {
        // Proof for optimal Dijkstra :
        // Label cost is the shortest road cost's from the origin to this label.
        // Supplementary proof for optimal AStar :
        // Estimated cost to destination must be inferior or equals to their real cost for each labels.
        double cost = 0;
        double arc_cost = 0;
        Point dest = aStarSolution.getPath().getDestination().getPoint();
        double est = 0;
        int maxSpeed = 0;
        // Maximum allowed speed
        if (data.getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED) {
            maxSpeed = data.getMaximumSpeed();
        }
        // Or maximum graph speed
        if (data.getGraph().getGraphInformation().hasMaximumSpeed()) {
            maxSpeed = data.getGraph().getGraphInformation().getMaximumSpeed();
        }
        List<Arc> list = dijkstraSolution.getPath().getArcs();

        // Same cost for Dijkstra and AStar
        assertEquals("o = "+dijkstraSolution.getPath().getOrigin().getId()+", d = "+dijkstraSolution.getPath().getDestination().getId(),
                dijkstraSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);

        // For each arc in path, assert if they really are the shortest allowed between their origin and destination
        for (Arc arc : list) {
            double min = Double.POSITIVE_INFINITY;
            Arc road = null;
            for (Arc other : arc.getOrigin().getSuccessors()) {
                if (other.getDestination().getId() == arc.getDestination().getId() &&
                        data.isAllowed(other) &&
                        min > data.getCost(other)) {
                    road = other;
                    min = data.getCost(other);
                }
            }
            // Assert if we found the same arc and cost
            assertEquals(road, arc);
            assertEquals(min, data.getCost(arc), 0);
            arc_cost += data.getCost(arc);
            cost += min;
        }
        assertEquals(cost, arc_cost, 0);

        // Assert if estimation is inferior or equals to real cost (with a delta for floating comparaison)
        cost = 0;
        for (int i = list.size()-1; i >= 0; i--) {
            cost += data.getCost(list.get(i));
            if (maxSpeed != 0) {
                est = (list.get(i).getOrigin().getPoint().distanceTo(dest) * 3600.0) / (maxSpeed * 1000.0);
            }
            assertTrue(cost >= est);
        }
    }

    @Before
    public void initAll() throws IOException {
        super.initAll();
        midi = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../maps/midi-pyrenees.mapgr")))).read();
    }

    /**
     *
     * @throws IOException
     */
    @Test
    public void testHGINSAAirportShortest() throws IOException {
        int insa = 10991;
        int airport = 89149;

        // Shortest path from INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(1));
        ShortestPathSolution dijkstraSolution = testINSAAirportLength(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSAAirportLength(new AStarAlgorithm(data));

        testShortest(data, dijkstraSolution, aStarSolution);
    }

    @Test
    public void testHGINSAAirportFastest() throws IOException {
        int insa = 10991;
        int airport = 89149;

        // Fastest path from INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution dijkstraSolution = testINSAAirportTime(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSAAirportTime(new AStarAlgorithm(data));

        testFastest(data, dijkstraSolution, aStarSolution);
    }

    @Test
    public void testHGINSABikiniShortest() throws IOException {
        int insa = 10991;
        int bikini = 63104;

        // Shortest path from INSA to Bikini on any road
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution dijkstraSolution = testINSABikiniCanal(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSABikiniCanal(new AStarAlgorithm(data));

        testShortest(data, dijkstraSolution, aStarSolution);
    }

    @Test
    public void testHGINSABikiniFastest() throws IOException {
        int insa = 10991;
        int bikini = 63104;

        // Fastest path from INSA to Bikini restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution dijkstraSolution = testINSABikiniTimeCar(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSABikiniTimeCar(new AStarAlgorithm(data));

        testFastest(data, dijkstraSolution, aStarSolution);
    }

    @Test
    public void testMidiRandomShortest() {
        Random rand = new Random();
        for (int i = 0; i < 50; ) {
            int origin = rand.nextInt(rand.nextInt(midi.size()));
            int destination = rand.nextInt(rand.nextInt(midi.size()));

            ShortestPathData data = new ShortestPathData(midi,
                    midi.get(origin),
                    midi.get(destination),
                    ArcInspectorFactory.getAllFilters().get(rand.nextInt(2)));
            ShortestPathSolution dijkstraSolution = new DijkstraAlgorithm(data).run();
            ShortestPathSolution aStarSolution = new AStarAlgorithm(data).run();
            if (dijkstraSolution.isFeasible() && aStarSolution.isFeasible()) {
                testShortest(data, dijkstraSolution, aStarSolution);
                i++;
            }
        }
    }

    @Test
    public void testMidiRandomFastest() {
        Random rand = new Random();
        for (int i = 0; i < 50; ) {
            int origin = rand.nextInt(rand.nextInt(midi.size()));
            int destination = rand.nextInt(rand.nextInt(midi.size()));
            int mode = rand.nextInt(3)+2;

            ShortestPathData data = new ShortestPathData(midi,
                    midi.get(origin),
                    midi.get(destination),
                    ArcInspectorFactory.getAllFilters().get(mode));
            ShortestPathSolution dijkstraSolution = new DijkstraAlgorithm(data).run();
            ShortestPathSolution aStarSolution = new AStarAlgorithm(data).run();
            if (dijkstraSolution.isFeasible() && aStarSolution.isFeasible()) {
                testFastest(data, dijkstraSolution, aStarSolution);
                i++;
            }
        }
    }
}
