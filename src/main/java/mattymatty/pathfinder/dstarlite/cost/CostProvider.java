package mattymatty.pathfinder.dstarlite.cost;

import mattymatty.pathfinder.dstarlite.DStarLite;
import mattymatty.pathfinder.dstarlite.state.State;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class CostProvider {

    protected final Set<DStarLite> pathfinders = Collections.newSetFromMap(new WeakHashMap<>());

    public void addPathfinder(DStarLite pathfinder){this.pathfinders.add(pathfinder);}
    /*
     * Returns the cost of moving from state a to state b.
     */
    public abstract double cost(State start, State end);
    /*
     * Returns true if the cell is occupied (non-traversable), false
     * otherwise.
     */
    public abstract boolean occupied(State u);
}
