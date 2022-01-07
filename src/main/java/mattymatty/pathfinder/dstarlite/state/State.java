package mattymatty.pathfinder.dstarlite.state;

import mattymatty.pathfinder.dstarlite.Pair;

import java.util.Collections;
import java.util.List;

public abstract class State extends Object implements Comparable<State>, java.io.Serializable, Cloneable
{
	public static final State EMPTYSTATE = new State() {
		@Override
		public boolean eq(State s2) {
			return this == s2;
		}

		@Override
		public boolean neq(State s2) {
			return !this.eq(s2);
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public double distanceFrom(State other) {
			return Double.POSITIVE_INFINITY;
		}
		@Override
		public double approxDistanceFrom(State other) {
			return Double.POSITIVE_INFINITY;
		}

		@Override
		public List<State> getSuccessors() {
			return Collections.emptyList();
		}

		@Override
		public List<State> getPredecessors() {
			return Collections.emptyList();
		}
	};

	public Pair<Double, Double> k = new Pair<>(0.0,0.0);

	//Default constructor
	public State()
	{

	}

	//Overloaded constructor
	public State( Pair<Double,Double> k)
	{
		this.k = k;
	}

	//Overloaded constructor
	public State(State other)
	{
		this.k = other.k;
	}

	@Override
	public State clone() {
		State clone;
		try{
			clone = (State)super.clone();
		}catch (CloneNotSupportedException ex){
			throw new AssertionError(ex);
		}
		clone.k = this.k;

		return clone;
	}

	//Equals
	public abstract boolean eq(State s2);

	//Not Equals
	public abstract boolean neq(State s2);

	//Greater than
	public boolean gt(final State s2)
	{
		if (k.first()-0.00001 > s2.k.first()) return true;
		else if (k.first() < s2.k.first()-0.00001) return false;
		return k.second() > s2.k.second();
	}

	//Less than or equal to
	public boolean lte(final State s2)
	{
		if (k.first() < s2.k.first()) return true;
		else if (k.first() > s2.k.first()) return false;
		return k.second() < s2.k.second() + 0.00001;
	}

	//Less than
	public boolean lt(final State s2)
	{
		if (k.first() + 0.000001 < s2.k.first()) return true;
		else if (k.first() - 0.000001 > s2.k.first()) return false;
		return k.second() < s2.k.second();
	}

	//CompareTo Method. This is necessary when this class is used in a priority queue
	public int compareTo(State other)
	{
		//This is a modified version of the gt method
		if (k.first()-0.00001 > other.k.first()) return 1;
		else if (k.first() < other.k.first()-0.00001) return -1;
		if (k.second() > other.k.second()) return 1;
		else if (k.second() < other.k.second()) return -1;
		return 0;
	}


	public abstract List<State> getSuccessors();

	public abstract List<State> getPredecessors();

	public abstract double distanceFrom(State other);

	public abstract double approxDistanceFrom(State other);

	//Override the CompareTo function for the HashMap usage
	@Override
	public abstract int hashCode();

	@Override
	public boolean equals(Object aThat) {
		//check for self-comparison
		if ( this == aThat ) return true;
		//use instanceof instead of getClass here for two reasons
		//1. if need be, it can match any supertype, and not just one class;
		//2. it renders an explict check for "that == null" redundant, since
		//it does the check for null already - "null instanceof [type]" always
		//returns false. (See Effective Java by Joshua Bloch.)
		if ( !(aThat instanceof State) ) return false;
		//Alternative to the above line :
		//if ( aThat == null || aThat.getClass() != this.getClass() ) return false;
		return false;

	}

}
