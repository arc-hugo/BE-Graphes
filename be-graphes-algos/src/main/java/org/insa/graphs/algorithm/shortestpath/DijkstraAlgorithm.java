package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    protected final ShortestPathData data;

    protected BinaryHeap<Label> heap;
    protected Label[] labels;
    protected List<Node> nodes;

    protected Node origin;
    protected Node destination;

    protected Label label_origin;
    protected Label label_destination;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);

        // Initiate data, nodes, labels and heap
        this.data = data;
        this.heap = new BinaryHeap<>();
        this.labels = new Label[data.getGraph().size()];
        this.nodes = data.getGraph().getNodes();

        this.origin = this.data.getOrigin();
        this.destination = this.data.getDestination();
    }

    @Override
    protected ShortestPathSolution doRun() {
        // Initiate variables
        ShortestPathSolution solution;
        Path path;

        // Initiate if we don't come from AStar
        if (this.label_origin == null) {
            this.label_origin = new Label(origin, 0, null);

            if (this.origin.getId() != this.destination.getId())
                this.label_destination = new Label(destination);
            else
                this.label_destination = this.label_origin;

            for (Node current : this.nodes) {
                if (current.getId() == this.origin.getId()) {
                    this.labels[current.getId()] = this.label_origin;
                } else if (current.getId() == this.destination.getId()) {
                    this.labels[current.getId()] = this.label_destination;
                } else {
                    this.labels[current.getId()] = new Label(current);
                }
            }
        }

        this.heap.insert(this.label_origin);
        notifyOriginProcessed(this.origin);

        // Iterate Dijkstra while destination is not marked or heap is not Empty
        while (!this.label_destination.isMarked() && !this.heap.isEmpty()) {
            // Extract min from heap and marked it
            Label min = this.heap.deleteMin();
            min.setMarked();
            notifyNodeMarked(min.getNode());

            // Update all successors of min
            for (Arc current : min.getNode().getSuccessors()) {
                Label label = this.labels[current.getDestination().getId()];
                if (!label.isMarked() && this.data.isAllowed(current)) {
                    if (label.getCost() > (min.getCost() + this.data.getCost(current))) {
                        if (label.getParent() == null) {
                            if (label.getNode().getId() == this.destination.getId())
                                notifyDestinationReached(this.destination);
                            else
                                notifyNodeReached(label.getNode());
                        } else {
                            this.heap.remove(label);
                        }
                        // New cost
                        double cost = min.getCost() + this.data.getCost(current);
                        label.changeParent(current, cost);
                        this.heap.insert(label);
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
