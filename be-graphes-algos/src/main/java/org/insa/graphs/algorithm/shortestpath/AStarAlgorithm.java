package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.*;

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
        // Search for maximum speed (kmph) if we are in time mode
        int maxSpeed = 0;
        if (this.data.getMode() == AbstractInputData.Mode.TIME) {
            System.out.println(this.data.getGraph().getGraphInformation().getMaximumSpeed());
            if (this.data.getGraph().getGraphInformation().hasMaximumSpeed())
                maxSpeed = this.data.getGraph().getGraphInformation().getMaximumSpeed();
        }
        // Compute the estimated distance cost to destination before Dijkstra (est <= cost)
        Point dest = this.label_destination.getNode().getPoint();
        for (Label label : this.labels) {
            Point point = label.getNode().getPoint();
            double est = point.distanceTo(dest);
            if (this.data.getMode() == AbstractInputData.Mode.TIME)
                if (maxSpeed != 0)
                    est = est * 3600 / (maxSpeed * 1000);
                else
                    est = 0;
            label.setDestinationCost(est);
        }
        return super.doRun();
    }
}
