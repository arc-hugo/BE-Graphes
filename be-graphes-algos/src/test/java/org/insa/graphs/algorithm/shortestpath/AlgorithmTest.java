package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.model.*;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.junit.Before;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;

import static org.junit.Assert.*;

public class AlgorithmTest {
    // Small graphs
    protected static Graph graph, hautegaronne, insa, frenchpolynesia;

    // List of nodes
    protected static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    protected static Arc a2b, a2d, b2c, b2d, c2e, d2b, d2c, d2e, e2a, e2c;

    // Shortest path from A to another node
    protected static Path shortest2a, shortest2b, shortest2c, shortest2d, shortest2e;

    /**
     * Init all attributes with custom graph and files from the maps folder
     * @throws IOException if files are not founds or invalids
     */
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
            nodes[i] = new Node(i, new Point(0,0));

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
        insa = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../maps/insa.mapgr")))).read();
        frenchpolynesia = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../maps/french-polynesia.mapgr")))).read();

        // Create path
        shortest2a = new Path(graph, nodes[0]);
        shortest2b = new Path(graph, Arrays.asList(a2d, d2b));
        shortest2c = new Path(graph, Arrays.asList(a2d, d2b, b2c));
        shortest2d = new Path(graph, Collections.singletonList(a2d));
        shortest2e = new Path(graph, Arrays.asList(a2d, d2e));

    }

    public ShortestPathSolution testValidAtoA(ShortestPathAlgorithm algorithm) {
        // A --> A
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2a.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2a.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testValidAtoB(ShortestPathAlgorithm algorithm) {
        // A --> B
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2b.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2b.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testValidAtoC(ShortestPathAlgorithm algorithm) {
        // A --> C
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2c.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2c.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testValidAtoD(ShortestPathAlgorithm algorithm) {
        // A --> D
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2d.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2d.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testValidAtoE(ShortestPathAlgorithm algorithm) {
        // A --> E
        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2e.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2e.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testInvalidAtoB(ShortestPathAlgorithm algorithm) {
        // A -X-> B
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());

        return solution;
    }

    public ShortestPathSolution testInvalidAtoC(ShortestPathAlgorithm algorithm) {
        // A -X-> C
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());

        return solution;
    }

    public ShortestPathSolution testInvalidAtoD(ShortestPathAlgorithm algorithm) {
        // A -X-> D
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());

        return solution;
    }

    public ShortestPathSolution testInvalidAtoE(ShortestPathAlgorithm algorithm) {
        // A -X-> E
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());

        return solution;
    }

    public ShortestPathSolution testINSAAeroportTime(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_aeroport_time.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testINSAAeroportLength(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_aeroport_length.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testINSABikiniCanal(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_bikini_canal.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testINSABikiniTimeCar(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_bikini_time_car.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testRangueilINSA(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31insa_rangueil_insa.path"))))
                .readPath(insa);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testRangueilR2(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31insa_rangueil_r2.path"))))
                .readPath(insa);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testPapeetePihau(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_pf_papeete_pihau_shortest.path"))))
                .readPath(frenchpolynesia);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        return solution;
    }

    public ShortestPathSolution testPapeeteFare(ShortestPathAlgorithm algorithm) {
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());

        return solution;
    }
}
