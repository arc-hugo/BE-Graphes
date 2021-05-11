package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.*;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DijkstraAlgorithmTest {

    // Small graph
    protected static Graph graph, hautegaronne;

    // List of nodes
    protected static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    protected static Arc a2b, a2d, b2c, b2d, c2e, d2b, d2c, d2e, e2a, e2c;

    // Shortest path from A to another node
    protected static Path shortest2a, shortest2b, shortest2c, shortest2d, shortest2e;

    @Before
    public void initAll() throws IOException {
        // Restrict to acces mode FOOT only
        EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> restrictions = new EnumMap<>(AccessRestrictions.AccessMode.class);
        for (AccessRestrictions.AccessMode mode: AccessRestrictions.AccessMode.values())
            restrictions.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);
        restrictions.put(AccessRestrictions.AccessMode.FOOT, AccessRestrictions.AccessRestriction.ALLOWED);

        // Init pedestrian information
        RoadInformation info = new RoadInformation(RoadInformation.RoadType.PEDESTRIAN, new AccessRestrictions(restrictions), true, 1, "");

        // Create nodes
        nodes = new Node[5];
        for (int i = 0; i < nodes.length; i++)
            nodes[i] = new Node(i, new Point(ThreadLocalRandom.current().nextFloat()*10,ThreadLocalRandom.current().nextFloat()*10));

        // Create arcs
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, info, null);
        a2d = Node.linkNodes(nodes[0], nodes[3], 5, info, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 1, info, null);
        b2d = Node.linkNodes(nodes[1], nodes[3], 2, info, null);
        c2e = Node.linkNodes(nodes[2], nodes[4], 4, info, null);
        d2b = Node.linkNodes(nodes[3], nodes[1], 3, info, null);
        d2c = Node.linkNodes(nodes[3], nodes[2], 9, info, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 2, info, null);
        e2a = Node.linkNodes(nodes[4], nodes[0], 3, info, null);
        e2c = Node.linkNodes(nodes[4], nodes[2], 5, info, null);

        // Create graphs
        graph = new Graph("test", "Test Map", Arrays.asList(nodes), null);
        hautegaronne = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../maps/haute-garonne.mapgr")))).read();

        // Create path
        shortest2a = new Path(graph, nodes[0]);
        shortest2b = new Path(graph, Arrays.asList(new Arc[]{a2d, d2b}));
        shortest2c = new Path(graph, Arrays.asList(new Arc[]{a2d, d2b, b2c}));
        shortest2d = new Path(graph, Arrays.asList(new Arc[]{a2d}));
        shortest2e = new Path(graph, Arrays.asList(new Arc[]{a2d, d2e}));

    }


    @Test
    public void testValid() {
        // Accessible path from origin to destination
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[0], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoA(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[1], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoB(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[2], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoC(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoD(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoE(new DijkstraAlgorithm(data));
    }

    public void testValidAtoA(ShortestPathAlgorithm algorithm) {
        // A --> A
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2a.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2a.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

    }

    public void testValidAtoB(ShortestPathAlgorithm algorithm) {
        // A --> B
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2b.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2b.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testValidAtoC(ShortestPathAlgorithm algorithm) {
        // A --> C
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2c.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2c.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testValidAtoD(ShortestPathAlgorithm algorithm) {
        // A --> D
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2d.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2d.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testValidAtoE(ShortestPathAlgorithm algorithm) {
        // A --> E
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2e.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2e.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    @Test
    public void testInvalid() {
        // Unaccessible path from origin to destination (no pedestrian roads)

        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[1], ArcInspectorFactory.getAllFilters().get(0));
        testInvalidAtoB(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[2], ArcInspectorFactory.getAllFilters().get(0));
        testInvalidAtoC(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        testInvalidAtoD(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(0));
        testInvalidAtoE(new DijkstraAlgorithm(data));
    }

    public void testInvalidAtoB(ShortestPathAlgorithm algorithm) {
        // A -X-> B
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[1], ArcInspectorFactory.getAllFilters().get(3));
        ShortestPathSolution solution = new DijkstraAlgorithm(data).run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
    }

    public void testInvalidAtoC(ShortestPathAlgorithm algorithm) {
        // A -X-> C
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[2], ArcInspectorFactory.getAllFilters().get(1));
        ShortestPathSolution solution = new DijkstraAlgorithm(data).run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
    }

    public void testInvalidAtoD(ShortestPathAlgorithm algorithm) {
        // A -X-> D
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(1));
        ShortestPathSolution solution = new DijkstraAlgorithm(data).run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
    }

    public void testInvalidAtoE(ShortestPathAlgorithm algorithm) {
        // A -X-> E
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(1));
        ShortestPathSolution solution = new DijkstraAlgorithm(data).run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
    }

    @Test
    public void testDijkstraAlgorithmHauteGaronne() throws IOException {
        int insa = 10991;
        int airport = 89149;
        int bikini = 0;
        // Shortest length from INSA to Airport
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(1));
        testINSAAeroportLength(new DijkstraAlgorithm(data));

        // Shortest time from INSA to Airport
        data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(3));
        testINSAAeroportTime(new DijkstraAlgorithm(data));

    }

    private void testINSAAeroportTime(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_aeroport_time.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testINSAAeroportLength(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_aeroport_length.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }
}
