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

public class AStarAlgorithmTest extends DijkstraAlgorithmTest{

    @Test
    public void testAStarAlgorithmValid() {
        // Accessible path from origin to destination
        ShortestPathData data = new ShortestPathData(graph, nodes[0], nodes[0], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoA(new AStarAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[1], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoB(new AStarAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[2], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoC(new AStarAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoD(new AStarAlgorithm(data));

        data = new ShortestPathData(graph, nodes[0], nodes[4], ArcInspectorFactory.getAllFilters().get(0));
        testValidAtoE(new AStarAlgorithm(data));
    }
}
