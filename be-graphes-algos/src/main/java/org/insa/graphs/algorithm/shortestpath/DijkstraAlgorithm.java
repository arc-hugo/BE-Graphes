package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        // Initiate variables
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Path path = null;
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        Label[] labels = new Label[data.getGraph().size()];
        List<Node> nodes = data.getGraph().getNodes();
        Node origin = data.getOrigin();
        Node destination = data.getDestination();

        // Initiate labels and heap
        Label label_origin = new Label(origin, 0, null);
        Label label_destination;
        if (origin.getId() != destination.getId())
            label_destination = new Label(destination);
        else
            label_destination = label_origin;
        for (Node current : nodes) {
            if (current.getId() == origin.getId()) {
                labels[current.getId()] = label_origin;
            } else if (current.getId() == destination.getId()) {
                labels[current.getId()] = label_destination;
            } else {
                labels[current.getId()] = new Label(current);
            }
        }
        heap.insert(label_origin);
        notifyOriginProcessed(origin);

        // Iterate Djikstra while destination is not marked or heap is not Empty
        while (!label_destination.isMarked() && !heap.isEmpty()) {
            // Extract min from heap and marked it
            Label min = heap.deleteMin();
            min.setMarked();
            notifyNodeMarked(min.getNode());

            // Update all successors of min
            for (Arc current : min.getNode().getSuccessors()) {
                Label label = labels[current.getDestination().getId()];
                if (!label.isMarked() && data.isAllowed(current)) {
                    if (label.getCost() > (min.getCost() + data.getCost(current))) {
                        if (label.getParent() == null) {
                            if (label.getNode().getId() == destination.getId())
                                notifyDestinationReached(destination);
                            else
                                notifyNodeReached(label.getNode());
                        } else {
                            heap.remove(label);
                        }
                        label.changeParent(current, min.getCost() + data.getCost(current));
                        heap.insert(label);
                    }
                }
            }
        }

        // Verify if path is feasible and create solution
        if (label_destination.isMarked()) {
            ArrayList<Arc> arcs = new ArrayList<>();
            Label label = label_destination;
            while (label.getParent() != null) {
                arcs.add(0, label.getParent());
                label = labels[label.getParent().getOrigin().getId()];
            }

            path = new Path(data.getGraph(), arcs);
            solution = new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, path);
        } else {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        }

        return solution;
    }

}
