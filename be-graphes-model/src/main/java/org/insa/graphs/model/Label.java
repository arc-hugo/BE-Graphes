package org.insa.graphs.model;

/**
 * <p>
 *     Label class
 * </p>
 * <p>
 *     This class is meant to implement the separation principle of the Djikstra algorithm by labeling the graph's nodes.
 * </p>
 */
public class Label implements Comparable<Label> {

    // Node associated
    final private Node associatedNode;

    // Node's mark
    private boolean mark;

    // Shortest path cost to this node
    private double cost;

    // Arc of previous node
    private Arc parent;

    /**
     * Associate a Label to a Node with inifinite cost and no parent.
     * @param sommetCourant Associated node.
     */
    public Label (Node sommetCourant) {
        this.associatedNode = sommetCourant;
        this.mark = false;
        this.cost = Float.POSITIVE_INFINITY;
        this.parent = null;
    }

    /**
     * Associate a Label to a Node with custom cost and parent.
     * @param sommetCourant Associated node.
     * @param cost Associated cost.
     * @param parent Arc with parent node has its origin.
     */
    public Label (Node sommetCourant, float cost, Arc parent) {
        this.associatedNode = sommetCourant;
        this.mark = false;
        this.cost = cost;
        this.parent = parent;
    }

    /**
     * @return Node associated top this label.
     */
    public Node getNode() { return  this.associatedNode; }

    /**
     * @return Cost associated to this label.
     */
    public double getCost() { return this.cost; }

    /**
     * @return Arc of previous node
     */
    public Arc getParent() { return this.parent; }

    /**
     * @return If this label is marked.
     */
    public boolean isMarked() { return this.mark; }

    /**
     * Set this label as marked.
     */
    public void setMarked() { this.mark = true; }

    /**
     * Set this label parents Node and change it's cost.
     * @param parent Arc with parent node has its origin.
     * @param cost New cost.
     */
    public void changeParent(Arc parent, double cost) {
        this.parent = parent;
        this.cost = cost;
    }

    @Override
    public int compareTo(Label o) {
        return Double.compare(this.getCost(), o.getCost());
    }
}
