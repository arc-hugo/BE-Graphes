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

    @Override
    public int compareTo(Label o) {
        int compare = Double.compare(this.getTotalCost(), o.getTotalCost());
        if (compare == 0)
            compare = Double.compare(this.getDestinationCost(), o.getDestinationCost());
        return compare;
    }
}
