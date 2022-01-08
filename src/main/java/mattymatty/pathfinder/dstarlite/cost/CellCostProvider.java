package mattymatty.pathfinder.dstarlite.cost;

import mattymatty.pathfinder.dstarlite.state.State;

import java.util.HashMap;

public class CellCostProvider extends CostProvider{

    private final double C1;
    private final HashMap<State,Double> costHash = new HashMap<>();

    public CellCostProvider(double c1) {
        C1 = c1;
    }

    /*
     * Returns the cost of moving from state a to state b. This could be
     * either the cost of moving off state a or onto state b, we went with the
     * former.
     */
    @Override
    public double cost(State start, State end) {
        double scale = start.approxDistanceFrom(end);

        if (!costHash.containsKey(start)) return scale*C1;
        return scale*costHash.get(start);
    }

    /*
     * Returns true if the cell is occupied (non-traversable), false
     * otherwise. Non-traversable are marked with a cost < 0
     */
    @Override
    public boolean occupied(State u) {
        if (!costHash.containsKey(u)) return false;
        return costHash.get(u) < 0.0;
    }

    public void setCost(State u, double cost) {
        costHash.put(u,cost);
        this.pathfinders.forEach(p->p.updateCell(u));
    }
}
