package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AStarAlgorithmTest {

    // Small graph
    protected static Graph graph;

    // List of nodes
    protected static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    protected static Arc a2b, a2d, b2c, b2d, c2e, d2b, d2c, d2e, e2a, e2c;

    // Shortest path from A to another node
    protected static Path shortest2a, shortest2b, shortest2c, shortest2d, shortest2e;

    @Before
    public void initAll() {
        // Restrict to acces mode FOOT only
        EnumMap<AccessRestrictions.AccessMode, AccessRestrictions.AccessRestriction> restrictions = new EnumMap<>(AccessRestrictions.AccessMode.class);
        for (AccessRestrictions.AccessMode mode: AccessRestrictions.AccessMode.values())
            restrictions.put(mode, AccessRestrictions.AccessRestriction.FORBIDDEN);
        restrictions.put(AccessRestrictions.AccessMode.FOOT, AccessRestrictions.AccessRestriction.ALLOWED);

        // Init pedestrian information
        RoadInformation info = new RoadInformation(RoadInformation.RoadType.PEDESTRIAN, new AccessRestrictions(restrictions), true, 1, "");

        // Create nodes
        nodes = new Node[] {
                new Node(0, new Point(0, 0)),
                new Node(1, new Point(0, 0)),
                new Node(2, new Point(0, 0)),
                new Node(3, new Point(0, 0)),
                new Node(4, new Point(0, 0))
        };
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

        // Create graph
        graph = new Graph("test", "Test Map", Arrays.asList(nodes), null);

        // Create path
        shortest2a = new Path(graph, nodes[0]);
        shortest2b = new Path(graph, Arrays.asList(new Arc[]{a2d, d2b}));
        shortest2c = new Path(graph, Arrays.asList(new Arc[]{a2d, d2b, b2c}));
        shortest2d = new Path(graph, Arrays.asList(new Arc[]{a2d}));
        shortest2e = new Path(graph, Arrays.asList(new Arc[]{a2d, d2e}));

    }

    @Test
    public void testAStarAlgorithmValid() {
        // Accessible path from origin to destination

        // A -> A
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[0], ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution solution = new AStarAlgorithm(data).run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2a.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2a.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        // A -> B
        data = new ShortestPathData(graph, nodes[0], nodes[1], ArcInspectorFactory.getAllFilters().get(0));
        solution = new AStarAlgorithm(data).run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2b.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2b.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        // Bellman-Ford comparison
        ShortestPathSolution alt = new BellmanFordAlgorithm(data).run();
        assertEquals(alt.getPath().getLength(), solution.getPath().getLength(), 0);
        assertEquals(alt.getPath().getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);


        // A -> C
        data = new ShortestPathData(graph, nodes[0], nodes[2], ArcInspectorFactory.getAllFilters().get(0));
        solution = new AStarAlgorithm(data).run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2c.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2c.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        // Bellman-Ford comparison
        alt = new BellmanFordAlgorithm(data).run();
        assertEquals(alt.getPath().getLength(), solution.getPath().getLength(), 0);
        assertEquals(alt.getPath().getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);


        // A -> D
        data = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        solution = new AStarAlgorithm(data).run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2d.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2d.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        // Bellman-Ford comparison
        alt = new BellmanFordAlgorithm(data).run();
        assertEquals(alt.getPath().getLength(), solution.getPath().getLength(), 0);
        assertEquals(alt.getPath().getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);


        // A -> E
        data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(0));
        solution = new AStarAlgorithm(data).run();
        assertTrue(solution.getPath().isValid());
        assertEquals(AbstractSolution.Status.FEASIBLE, solution.getStatus());
        assertEquals(shortest2e.getLength(), solution.getPath().getLength(), 0);
        assertEquals(shortest2e.getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);

        // Bellman-Ford comparison
        alt = new BellmanFordAlgorithm(data).run();
        assertEquals(alt.getPath().getLength(), solution.getPath().getLength(), 0);
        assertEquals(alt.getPath().getMinimumTravelTime(), solution.getPath().getMinimumTravelTime(), 0);
    }


}
