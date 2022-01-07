package mattymatty.pathfinder.dstarlite.cost;

import mattymatty.pathfinder.dstarlite.state.State;

public abstract class CostProvider {
    /*
     * Returns the cost of moving from state a to state b.
     */
    public abstract double cost(State start, State end);
    /*
     * Returns true if the cell is occupied (non-traversable), false
     * otherwise.
     */
    public abstract boolean occupied(State u);
    public abstract void setCost(State u, double cost);
}
