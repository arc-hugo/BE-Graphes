package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.*;
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

    private void testShortest (ShortestPathData data, ShortestPathSolution dijkstraSolution, ShortestPathSolution aStarSolution, ArcInspector arcInspector) {
        // Proof for optimal Dijkstra :
        // Label cost is the shortest road cost's from the origin to this label.
        // Supplementary proof for optimal AStar :
        // Estimated cost to destination must be inferior or equals to real cost to destination of each labels.
        // Same cost for Dijkstra and AStar
        float cost = 0;
        Point dest = aStarSolution.getPath().getDestination().getPoint();
        float est;
        Random rand = new Random();
        List<Arc> list = dijkstraSolution.getPath().getArcs();

        // Asset if Dijkstra and AStar are equivalent
        assertEquals(dijkstraSolution.getPath().getLength(), aStarSolution.getPath().getLength(), 0);

        // Dijkstra :
        // Get a series of nodes between origin and destination, for each nodes :
        // - launch dijkstra from origin to node
        // - launch dijkstra from node to destination
        // - assert if the concatenation of both solution is equals to the given one
        for (int i = 0; i < 10; i++) {
            int arc = rand.nextInt(list.size());

            // Origin -> Node
            ShortestPathData o_to_n = new ShortestPathData(data.getGraph(), dijkstraSolution.getInputData().getOrigin(), list.get(arc).getDestination(), arcInspector);
            ShortestPathSolution o_to_n_solution = new DijkstraAlgorithm(o_to_n).run();

            // Node -> Destination
            ShortestPathData n_to_d = new ShortestPathData(data.getGraph(), list.get(arc).getDestination(), dijkstraSolution.getInputData().getDestination(), arcInspector);
            ShortestPathSolution n_to_d_solution = new DijkstraAlgorithm(n_to_d).run();

            assertEquals(Path.concatenate(o_to_n_solution.getPath(), n_to_d_solution.getPath()).getLength(), dijkstraSolution.getPath().getLength(), 0);
        }


        // AStar :
        // Assert if estimation is inferior or equals to real cost for each nodes of the path (with a delta for floating comparaison)
        for (Arc arc : list) {
            est = (float) arc.getOrigin().getPoint().distanceTo(dest);
            assertTrue(aStarSolution.getPath().getLength()-cost+0.01+" >= "+est,
                    aStarSolution.getPath().getLength()-cost+0.01 >= est);

            // Update cost
            cost += arc.getLength();
        }
    }

    private void testFastest (ShortestPathData data, ShortestPathSolution dijkstraSolution, ShortestPathSolution aStarSolution, ArcInspector arcInspector) {
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
        Random rand = new Random();
        List<Arc> list = dijkstraSolution.getPath().getArcs();

        // Same cost for Dijkstra and AStar
        assertEquals(dijkstraSolution.getPath().getMinimumTravelTime(), aStarSolution.getPath().getMinimumTravelTime(), 0);

        // Dijkstra :
        // Get a series of nodes between origin and destination, for each nodes :
        // - launch dijkstra from origin to node
        // - launch dijkstra from node to destination
        // - assert if the concatenation of both solution is equals to the given one
        for (int i = 0; i < 10; i++) {
            int arc = rand.nextInt(list.size());

            // Origin -> Node
            ShortestPathData o_to_n = new ShortestPathData(data.getGraph(), dijkstraSolution.getInputData().getOrigin(), list.get(arc).getDestination(), arcInspector);
            ShortestPathSolution o_to_n_solution = new DijkstraAlgorithm(o_to_n).run();

            // Node -> Destination
            ShortestPathData n_to_d = new ShortestPathData(data.getGraph(), list.get(arc).getDestination(), dijkstraSolution.getInputData().getDestination(), arcInspector);
            ShortestPathSolution n_to_d_solution = new DijkstraAlgorithm(n_to_d).run();

            assertEquals(Path.concatenate(o_to_n_solution.getPath(), n_to_d_solution.getPath()).getMinimumTravelTime(), dijkstraSolution.getPath().getMinimumTravelTime(), 0);
        }

        // AStar :
        // Assert if estimation is inferior or equals to real cost for each nodes of the path
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

    @Test
    public void testHGINSAAirportShortest() throws IOException {
        int insa = 10991;
        int airport = 89149;
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(1);

        // Shortest path from INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), arcInspector);
        ShortestPathSolution dijkstraSolution = testINSAAirportLength(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSAAirportLength(new AStarAlgorithm(data));

        testShortest(data, dijkstraSolution, aStarSolution, arcInspector);
    }

    @Test
    public void testHGINSAAirportFastest() throws IOException {
        int insa = 10991;
        int airport = 89149;
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(3);

        // Fastest path from INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), arcInspector);
        ShortestPathSolution dijkstraSolution = testINSAAirportTime(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSAAirportTime(new AStarAlgorithm(data));

        testFastest(data, dijkstraSolution, aStarSolution, arcInspector);
    }

    @Test
    public void testHGINSABikiniShortest() throws IOException {
        int insa = 10991;
        int bikini = 63104;
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);

        // Shortest path from INSA to Bikini on any road
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), arcInspector);
        ShortestPathSolution dijkstraSolution = testINSABikiniCanal(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSABikiniCanal(new AStarAlgorithm(data));

        testShortest(data, dijkstraSolution, aStarSolution, arcInspector);
    }

    @Test
    public void testHGINSABikiniFastest() throws IOException {
        int insa = 10991;
        int bikini = 63104;
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(3);

        // Fastest path from INSA to Bikini restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), arcInspector);
        ShortestPathSolution dijkstraSolution = testINSABikiniTimeCar(new DijkstraAlgorithm(data));
        ShortestPathSolution aStarSolution = testINSABikiniTimeCar(new AStarAlgorithm(data));

        testFastest(data, dijkstraSolution, aStarSolution, arcInspector);
    }

    @Test
    public void testMidiRandomShortest() {
        Random rand = new Random();
        for (int i = 0; i < 50; ) {
            int origin = rand.nextInt(rand.nextInt(midi.size()));
            int destination = rand.nextInt(rand.nextInt(midi.size()));
            ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(rand.nextInt(2));

            ShortestPathData data = new ShortestPathData(midi,
                    midi.get(origin),
                    midi.get(destination),
                    arcInspector);
            ShortestPathSolution dijkstraSolution = new DijkstraAlgorithm(data).run();
            ShortestPathSolution aStarSolution = new AStarAlgorithm(data).run();
            if (dijkstraSolution.isFeasible() && aStarSolution.isFeasible()) {
                testShortest(data, dijkstraSolution, aStarSolution, arcInspector);
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
            ArcInspector arcInspector =  ArcInspectorFactory.getAllFilters().get(rand.nextInt(3)+2);

            ShortestPathData data = new ShortestPathData(midi,
                    midi.get(origin),
                    midi.get(destination),
                    arcInspector);
            ShortestPathSolution dijkstraSolution = new DijkstraAlgorithm(data).run();
            ShortestPathSolution aStarSolution = new AStarAlgorithm(data).run();
            if (dijkstraSolution.isFeasible() && aStarSolution.isFeasible()) {
                testFastest(data, dijkstraSolution, aStarSolution, arcInspector);
                i++;
            }
        }
    }
}
