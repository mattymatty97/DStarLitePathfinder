package mattymatty.pathfinder.dstarlite;

import mattymatty.pathfinder.dstarlite.cost.CostProvider;
import mattymatty.pathfinder.dstarlite.state.State;

import java.util.*;

public class DStarLite implements java.io.Serializable{

	//Private Member variables
	private final List<State> path = new ArrayList<State>();
	private final double C1;
	private double k_m;
	private State s_start = State.EMPTYSTATE;
	private State s_goal  = State.EMPTYSTATE;
	private State s_last  = State.EMPTYSTATE;
	private final int maxSteps;
	private final PriorityQueue<State>		openList = new PriorityQueue<State>();
	//Change back to private****
	public HashMap<State, CellInfo>	cellHash = new HashMap<State, CellInfo>();
	private final HashMap<State, Float>		openHash = new HashMap<State, Float>();

	private final Class stateClass;

	private final CostProvider costProvider;

	//Default constructor
	public DStarLite(Class stateClass, CostProvider costProvider)
	{
		this.costProvider = costProvider;
		this.costProvider.addPathfinder(this);
		this.stateClass = stateClass;
		maxSteps	= 80000;
		C1			= 1;
	}

	//Calculate Keys
	public void CalculateKeys()
	{
			
	}

	/*
	 * Initialise Method
	 * @params start and goal coordinates
	 */
	public synchronized void init(State s, State g)
	{
		if (!(s.getClass().isAssignableFrom(this.stateClass)))
			throw new AssertionError("You cannot use this State class with this Pathfinder Instance");
		if (!(g.getClass().isAssignableFrom(this.stateClass)))
			throw new AssertionError("You cannot use this State class with this Pathfinder Instance");
		cellHash.clear();
		path.clear();
		openHash.clear();
		while(!openList.isEmpty()) openList.poll();

		k_m = 0;

		s_start = s;
		s_goal = g;

		CellInfo tmp = new CellInfo();
		tmp.g   = 0;
		tmp.rhs = 0;

		cellHash.put(s_goal, tmp);

		tmp = new CellInfo();
		tmp.g = tmp.rhs = heuristic(s_start,s_goal);
		cellHash.put(s_start, tmp);
		s_start = calculateKey(s_start);

		s_last = s_start;

	}

	/*
	 * CalculateKey(state u)
	 * As per [S. Koenig, 2002]
	 */
	private State calculateKey(State u)
	{
		double val = Math.min(getRHS(u), getG(u));

		u.k.setFirst (val + heuristic(u,s_start) + k_m);
		u.k.setSecond(val);

		return u;
	}

	/*
	 * Returns the rhs value for state u.
	 */
	private double getRHS(State u)
	{
		if (u == s_goal) return 0;

		//if the cellHash doesn't contain the State u
		if (cellHash.get(u) == null)
			return heuristic(u, s_goal);
		return cellHash.get(u).rhs;
	}

	/*
	 * Returns the g value for the state u.
	 */
	private double getG(State u)
	{
		//if the cellHash doesn't contain the State u
		if (cellHash.get(u) == null)
			return heuristic(u,s_goal);
		return cellHash.get(u).g;
	}

	/*
	 * Pretty self explanatory, the heuristic we use is the 8-way distance
	 * scaled by a constant C1 (should be set to <= min cost)
	 */
	private double heuristic(State a, State b)
	{
		return a.approxDistanceFrom(b)*C1;
	}

	public synchronized boolean replan()
	{
		path.clear();

		int res = computeShortestPath();
		if (res < 0)
		{
			System.out.println("No Path to Goal");
			return false;
		}

		List<State> n;
		State cur = s_start;

		if (getG(s_start) == Double.POSITIVE_INFINITY)
		{
			System.out.println("No Path to Goal");
			return false;
		}

		while (cur.neq(s_goal))
		{
			path.add(cur);
			n = getSucc(cur);

			if (n.isEmpty())
			{
				System.out.println("No Path to Goal");
				return false;
			}

			double cmin = Double.POSITIVE_INFINITY;
			double tmin = 0;   
			State smin = State.EMPTYSTATE;

			for (State i : n)
			{
				double val  = costProvider.cost(cur, i);
				double val2 = i.distanceFrom(s_goal) + s_start.distanceFrom(i);
				val += getG(i);

				if (close(val,cmin)) {
					if (tmin > val2) {
						tmin = val2;
						cmin = val;
						smin = i;
					}
				} else if (val < cmin) {
					tmin = val2;
					cmin = val;
					smin = i;
				}
			}
			n.clear();
			cur = smin.clone();
			//cur = smin;
		}
		path.add(s_goal);
		return true;
	}

	/*
	 * As per [S. Koenig,2002] except for two main modifications:
	 * 1. We stop planning after a number of steps, 'maxsteps' we do this
	 *    because this algorithm can plan forever if the start is surrounded  by obstacles
	 * 2. We lazily remove states from the open list so we never have to iterate through it.
	 */
	private int computeShortestPath()
	{
		List<State> s;

		if (openList.isEmpty()) return 1;

		int k=0;
		while ((!openList.isEmpty()) &&
			   (openList.peek().lt(s_start = calculateKey(s_start))) ||
			   (getRHS(s_start) != getG(s_start))) {

			if (k++ > maxSteps) {
				System.out.println("At maxsteps");
				return -1;
			}

			State u;

			boolean test = (getRHS(s_start) != getG(s_start));

			//lazy remove
			while(true) {
				if (openList.isEmpty()) return 1;
				u = openList.poll();

				if (!isValid(u)) continue;
				if (!(u.lt(s_start)) && (!test)) return 2;
				break;
			}

			openHash.remove(u);

			State k_old = u.clone();

			if (k_old.lt(calculateKey(u))) { //u is out of date
				insert(u);
			} else if (getG(u) > getRHS(u)) { //needs update (got better)
				setG(u,getRHS(u));
				s = getPred(u);
				for (State i : s) {
					updateVertex(i);
				}
			} else {						 // g <= rhs, state has got worse
				setG(u, Double.POSITIVE_INFINITY);
				s = getPred(u);

				for (State i : s) {
					updateVertex(i);
				}
				updateVertex(u);
			}
		} //while
		return 0;
	}

	/*
	 * Update the position of the agent/robot.
	 * This does not force a replan.
	 */
	public synchronized void updateStart(State new_start)
	{
		if (!(new_start.getClass().isAssignableFrom(this.stateClass)))
			throw new AssertionError("You cannot use this State class with this Pathfinder Instance");
		s_start = new_start;

		k_m += heuristic(s_last,s_start);

		s_start = calculateKey(s_start);
		s_last = s_start;

	}

	/*
	 * This is somewhat of a hack, to change the position of the goal we
	 * first save all of the non-empty nodes on the map, clear the map, move the
	 * goal and add re-add all of the non-empty cells. Since most of these cells
	 * are not between the start and goal this does not seem to hurt performance
	 * too much. Also, it frees up a good deal of memory we are probably not
	 * going to use.
	 */
/*
	public void updateGoal(int x, int y)
	{
		List<Pair<ipoint2, Double> > toAdd = new ArrayList<Pair<ipoint2, Double> >();
		Pair<ipoint2, Double> tempPoint;

		for (Map.Entry<State,CellInfo> entry : cellHash.entrySet()) {
			if (!close(entry.getValue().cost, C1)) {
				tempPoint = new Pair(
							new ipoint2(entry.getKey().x,entry.getKey().y),
							entry.getValue().cost);
				toAdd.add(tempPoint);
			}
		}

		cellHash.clear();
		openHash.clear();

		while(!openList.isEmpty())
			openList.poll();

		k_m = 0;

		s_goal.x = x;
		s_goal.y = y;

		CellInfo tmp = new CellInfo();
		tmp.g = tmp.rhs = 0;
		tmp.cost = C1;

		cellHash.put(s_goal, tmp);

		tmp = new CellInfo();
		tmp.g = tmp.rhs = heuristic(s_start, s_goal);
		tmp.cost = C1;
		cellHash.put(s_start, tmp);
		s_start = calculateKey(s_start);

		s_last = s_start;

		Iterator<Pair<ipoint2,Double> > iterator = toAdd.iterator();
		while(iterator.hasNext()) {
			tempPoint = iterator.next();

			updateCell(tempPoint.first(), tempPoint.second());
		}


	}
*/

	/*
	 * As per [S. Koenig, 2002]
	 */
	private void updateVertex(State u)
	{
		List<State> s;

		if (u.neq(s_goal)) {
			s = getSucc(u);
			double tmp = Double.POSITIVE_INFINITY;
			double tmp2;

			for (State i : s) {
				tmp2 = getG(i) + costProvider.cost(u,i);
				if (tmp2 < tmp) tmp = tmp2;
			}
			if (!close(getRHS(u),tmp)) setRHS(u,tmp);
		}

		if (!close(getG(u),getRHS(u))) insert(u);
	}

	/*
	 * Returns true if state u is on the open list or not by checking if
	 * it is in the hash table.
	 */
	private boolean isValid(State u)
	{
		if (openHash.get(u) == null) return false;
		return close(keyHashCode(u), openHash.get(u));
	}

	/*
	 * Sets the G value for state u
	 */
	private void setG(State u, double g)
	{
		makeNewCell(u);
		cellHash.get(u).g = g;
	}

	/*
	 * Sets the rhs value for state u
	 */
	private void setRHS(State u, double rhs)
	{
		makeNewCell(u);
		cellHash.get(u).rhs = rhs;
	}

	/*
	 * Checks if a cell is in the hash table, if not it adds it in.
	 */
	private void makeNewCell(State u)
	{
		if (cellHash.get(u) != null) return;
		CellInfo tmp = new CellInfo();
		tmp.g = tmp.rhs = heuristic(u,s_goal);
		cellHash.put(u, tmp);
	}

	/*
	 * updateCell as per [S. Koenig, 2002]
	 */
	public synchronized void updateCell(State u)
	{
		if (!(u.getClass().isAssignableFrom(this.stateClass)))
			throw new AssertionError("You cannot use this State class with this Pathfinder Instance");
		if ((u.eq(s_start)) || (u.eq(s_goal))) return;

		makeNewCell(u);
		updateVertex(u);
	}

	/*
	 * addition for shared cost providers to not update a pathfinder if it does not use the cell
	 */
	public synchronized void updateCellIfPresent(State u)
	{
		if (!(u.getClass().isAssignableFrom(this.stateClass)))
			throw new AssertionError("You cannot use this State class with this Pathfinder Instance");
		if ((u.eq(s_start)) || (u.eq(s_goal))) return;

		if (cellHash.get(u) == null) return;

		updateVertex(u);
	}

	/*
	 * Inserts state u into openList and openHash
	 */
	private void insert(State u)
	{
		//iterator cur
		float csum;

		u = calculateKey(u);
		//cur = openHash.find(u);
		csum = keyHashCode(u);

		// return if cell is already in list. TODO: this should be
		// uncommented except it introduces a bug, I suspect that there is a
		// bug somewhere else and having duplicates in the openList queue
		// hides the problem...
		//if ((cur != openHash.end()) && (close(csum,cur->second))) return;


		openHash.put(u, csum);
		openList.add(u);
	}

	/*
	 * Returns the key hash code for the state u, this is used to compare
	 * a state that has been updated
	 */
	private float keyHashCode(State u)
	{
		return (float)(u.k.first() + 1193*u.k.second());
	}

	/*
	 * Returns a list of successor states for state u. Unless
	 * the cell is occupied, in which case it has no successors.
	 */
	private List<State> getSucc(State u){
		if (costProvider.occupied(u)) return Collections.emptyList();
		return u.getSuccessors();
	}

	/*
	 * Returns a list of predecessor states for state u. Occupied neighbours are then removed from the list.
	 */
	private List<State> getPred(State u){
		return u.getPredecessors().stream().filter(s->!costProvider.occupied(s)).toList();
	}


	/*
	 * Returns true if x and y are within 10E-5, false otherwise
	 */
	private boolean close(double x, double y)
	{
		if (x == Double.POSITIVE_INFINITY && y == Double.POSITIVE_INFINITY) return true;
		return (Math.abs(x-y) < 0.00001);
	}

	public List<State> getPath()
	{
		return path;
	}

/*
	public static void main(String[] args)
	{

		CellCostProvider costProvider = new CellCostProvider(1);
		DStarLite pf = new DStarLite(State2D.class, costProvider);
		State start = new State2D(0,1);
		State goal = new State2D(3,1);
		pf.init(start,goal);
		costProvider.setCost(new State2D(2,1), -1);
		costProvider.setCost(new State2D(2, 0), -1);
		costProvider.setCost(new State2D(2, 2), -1);
		costProvider.setCost(new State2D(3, 0), -1);

		System.out.println("Start node: (0,1)");
		System.out.println("End node: (3,1)");

		//Time the replanning
		long begin = System.currentTimeMillis();
		pf.replan();
		//pf.updateGoal(3, 2);
		long end = System.currentTimeMillis();

		System.out.println("Time: " + (end-begin) + "ms");

		List<State> path = pf.getPath();
		for (State i : path)
		{
			State2D p = (State2D)i;
			System.out.println("x: " + p.x + " y: " + p.y);
		}

	}

	public static void main(String[] args)
	{
		CellCostProvider costProvider = new CellCostProvider(1);
		DStarLite pf = new DStarLite(State3D.class, costProvider);
		State start = new State3D(0,0,1);
		State goal = new State3D(3,0,1);
		pf.init(start,goal);
		costProvider.setCost(new State3D(2,0,1), -1);
		costProvider.setCost(new State3D(2, 0,0), -1);
		costProvider.setCost(new State3D(2, 0,2), -1);
		costProvider.setCost(new State3D(3, 0,0), -1);

		System.out.println("Start node: (0,0,1)");
		System.out.println("End node: (3,0,1)");

		//Time the replanning
		long begin = System.currentTimeMillis();
		pf.replan();
		//pf.updateGoal(3, 2);
		long end = System.currentTimeMillis();

		System.out.println("Time: " + (end-begin) + "ms");

		List<State> path = pf.getPath();
		for (State i : path)
		{
			State3D p = (State3D)i;
			System.out.println("x: " + p.x + " y: " + p.y + " z: " + p.z);
		}

	}*/
}

