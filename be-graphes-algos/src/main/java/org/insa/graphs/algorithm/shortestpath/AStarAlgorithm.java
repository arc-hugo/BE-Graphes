package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.labels = new LabelStar[data.getGraph().size()];
        this.label_origin = new LabelStar(origin, 0, null);

        if (this.origin.getId() != this.destination.getId())
            this.label_destination = new LabelStar(destination);
        else
            this.label_destination = this.label_origin;

        for (Node current : this.nodes) {
            if (current.getId() == this.origin.getId()) {
                this.labels[current.getId()] = this.label_origin;
            } else if (current.getId() == this.destination.getId()) {
                this.labels[current.getId()] = this.label_destination;
            } else {
                this.labels[current.getId()] = new LabelStar(current);
            }
        }
    }

    @Override
    protected ShortestPathSolution doRun() {
        // Compute the estimated distance to destination before Dijkstra
        Point dest = this.label_destination.getNode().getPoint();
        for (int i = 0; i < this.labels.length; i++) {
            Point point = this.labels[i].getNode().getPoint();
            double dist = point.distanceTo(dest);
            this.labels[i].setDestinationCost(dist);
        }
        return super.doRun();
    }
}
