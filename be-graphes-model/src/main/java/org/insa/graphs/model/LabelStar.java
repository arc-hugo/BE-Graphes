package org.insa.graphs.model;

public class LabelStar extends Label {

    // Estimated cost to destination
    private double destinationCost;

    public LabelStar(Node associatedNode) {
        super(associatedNode);
        this.destinationCost = 0;
    }

    public LabelStar(Node associatedNode, double cost, Arc parent) {
        super(associatedNode, cost, parent);
        this.destinationCost = 0;
    }

    public void setDestinationCost(double cost) {
        this.destinationCost = cost;
    }

    @Override
    public double getDestinationCost() { return this.destinationCost; }
}
