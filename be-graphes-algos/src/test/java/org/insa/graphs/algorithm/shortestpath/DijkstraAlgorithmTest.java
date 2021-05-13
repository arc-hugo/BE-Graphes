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

import static org.junit.Assert.*;

public class DijkstraAlgorithmTest {

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
     * Throw IOException if files are not founds or invalids
     * @throws IOException
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
        shortest2b = new Path(graph, Arrays.asList(new Arc[]{a2d, d2b}));
        shortest2c = new Path(graph, Arrays.asList(new Arc[]{a2d, d2b, b2c}));
        shortest2d = new Path(graph, Arrays.asList(new Arc[]{a2d}));
        shortest2e = new Path(graph, Arrays.asList(new Arc[]{a2d, d2e}));

    }


    /**
     * Test valid set of path from the custom graph
     */
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

    /**
     * Test invalid set of path from the custom graph
     */
    @Test
    public void testInvalid() {
        // Unaccessible path from origin to destination (no pedestrian roads)

        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[1], ArcInspectorFactory.getAllFilters().get(1));
        testInvalidAtoB(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[2], ArcInspectorFactory.getAllFilters().get(1));
        testInvalidAtoC(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(1));
        testInvalidAtoD(new DijkstraAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(1));
        testInvalidAtoE(new DijkstraAlgorithm(data));
    }

    /**
     * Test set of paths from paths folder in the Haute Garonne map
     * Throws IOException if paths are not founds or invalids
     * @throws IOException
     */
    @Test
    public void testHauteGaronne() throws IOException {
        int insa = 10991;
        int airport = 89149;
        int bikini = 63104;
        // Shortest path from INSA to Airport restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(1));
        testINSAAeroportLength(new DijkstraAlgorithm(data));

        // Fastest path INSA to Airport restricted to roads open for cars
        data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(airport), ArcInspectorFactory.getAllFilters().get(3));
        testINSAAeroportTime(new DijkstraAlgorithm(data));

        // Shortest path from INSA to Bikini on any road
        data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), ArcInspectorFactory.getAllFilters().get(0));
        testINSABikiniCanal(new DijkstraAlgorithm(data));

        // Fastest path from INSA to Bikini restricted to roads open for cars
        data = new ShortestPathData(hautegaronne, hautegaronne.get(insa), hautegaronne.get(bikini), ArcInspectorFactory.getAllFilters().get(3));
        testINSABikiniTimeCar(new DijkstraAlgorithm(data));
    }

    /**
     * Test set of paths from paths folder in the INSA map
     * Throws IOException if paths are not founds or invalids
     * @throws IOException
     */
    @Test
    public void testINSA() throws IOException {
        int rangueil = 552;
        int entree = 254;
        int r2 = 526;
        // Shortest path from Rangueil to INSA restricted to roads open for cars
        ShortestPathData data = new ShortestPathData(insa, insa.get(rangueil), insa.get(entree), ArcInspectorFactory.getAllFilters().get(3));
        testRangueilINSA(new DijkstraAlgorithm(data));

        // Fastest and shortest path from Rangueil to R2 restricted to roads open for cars
        data = new ShortestPathData(insa, insa.get(rangueil), insa.get(r2), ArcInspectorFactory.getAllFilters().get(0));
        testRangueilR2(new DijkstraAlgorithm(data));
    }

    /**
     * Test set of paths from paths folder in the French Polynesia map
     * Throws IOException if paths are not founds or invalids
     * @throws IOException
     */
    @Test
    public void testFrenchPolynesia() throws IOException {
        int papeete = 3382;
        int fare = 3642;
        int pihau = 13979;
        // Shortest path from Papeete to Pihau on any road
        ShortestPathData data = new ShortestPathData(frenchpolynesia, frenchpolynesia.get(papeete), frenchpolynesia.get(pihau), ArcInspectorFactory.getAllFilters().get(0));
        testPapeetePihau(new DijkstraAlgorithm(data));

        // Invalid path from Papeete to Fare
        data = new ShortestPathData(frenchpolynesia, frenchpolynesia.get(papeete), frenchpolynesia.get(fare), ArcInspectorFactory.getAllFilters().get(0));
        testPapeeteFare(new DijkstraAlgorithm(data));
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

    public void testInvalidAtoB(ShortestPathAlgorithm algorithm) {
        // A -X-> B
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());
    }

    public void testInvalidAtoC(ShortestPathAlgorithm algorithm) {
        // A -X-> C
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());
    }

    public void testInvalidAtoD(ShortestPathAlgorithm algorithm) {
        // A -X-> D
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());
    }

    public void testInvalidAtoE(ShortestPathAlgorithm algorithm) {
        // A -X-> E
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());
    }

    public void testINSAAeroportTime(ShortestPathAlgorithm algorithm) throws IOException {
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

    public void testINSABikiniCanal(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_bikini_canal.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testINSABikiniTimeCar(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31_insa_bikini_time_car.path"))))
                .readPath(hautegaronne);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testRangueilINSA(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31insa_rangueil_insa.path"))))
                .readPath(insa);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testRangueilR2(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_fr31insa_rangueil_r2.path"))))
                .readPath(insa);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testPapeetePihau(ShortestPathAlgorithm algorithm) throws IOException {
        Path path = new BinaryPathReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/../paths/path_pf_papeete_pihau_shortest.path"))))
                .readPath(frenchpolynesia);

        ShortestPathSolution solution = algorithm.run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(path.getLength(), solution.getPath().getLength(), 0);
        assertEquals(path.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }

    public void testPapeeteFare(ShortestPathAlgorithm algorithm) {
        ShortestPathSolution solution = algorithm.run();
        assertEquals(AbstractSolution.Status.INFEASIBLE, solution.getStatus());
        assertNull(solution.getPath());
    }
}
