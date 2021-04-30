package org.insa.graphs.model;

public class LabelStar extends Label{

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

    public double getDestinationCost() { return this.destinationCost; }
}
