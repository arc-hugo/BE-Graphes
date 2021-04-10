package org.insa.graphs.model;

/**
 * <p>
 *     Label class
 * </p>
 * <p>
 *     This class is meant to implement the separation principle of the Djikstra algorithm by labeling the graph's nodes.
 * </p>
 */
public class Label {

    // Node associated
    private Node sommetCourant;

    // Node's mark
    private boolean mark;

    // Shortest path cost to this node
    private float cost;

    // Previous node
    private Arc parent;

    /**
     * Associate a Label to a Node with inifinite cost and no parent.
     * @param sommetCourant Associated node.
     */
    public Label (Node sommetCourant) {
        this.sommetCourant = sommetCourant;
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
        this.sommetCourant = sommetCourant;
        this.mark = false;
        this.cost = cost;
        this.parent = parent;
    }

    /**
     * @return Node associated top this label.
     */
    public Node getNode() { return  this.sommetCourant; }

    /**
     * @return Cost associated to this label.
     */
    public float getCost() { return this.cost; }

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
    public void changeParent(Arc parent, float cost) {
        this.parent = parent;
        this.cost = cost;
    }
}
