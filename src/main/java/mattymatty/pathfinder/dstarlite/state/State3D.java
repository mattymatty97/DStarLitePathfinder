package mattymatty.pathfinder.dstarlite.state;
/*
 * @author daniel beard
 * http://danielbeard.io
 * http://github.com/daniel-beard
 *
 * Copyright (C) 2012 Daniel Beard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

import mattymatty.pathfinder.dstarlite.Pair;
import mattymatty.pathfinder.dstarlite.utils.Geometry3D;

import java.util.LinkedList;
import java.util.List;

public class State3D extends State
{
	public int x=0;
	public int y=0;
	public int z=0;



	//Default constructor
	public State3D()
	{
		super();
	}

	//Overloaded constructor
	public State3D(int x, int y, int z)
	{
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	//Overloaded constructor
	public State3D(int x, int y, int z, Pair<Double,Double> k)
	{
		super(k);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//Overloaded constructor
	public State3D(State3D other)
	{
		super(other);
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	@Override
	public State3D clone() {
		State3D clone = (State3D) super.clone();
		clone.x = this.x;
		clone.y = this.y;
		clone.z = this.z;
		return clone;
	}

	//Equals
	public boolean eq(final State other)
	{
		if ( !(other instanceof State3D s2) ) return false;
		return ((this.x == s2.x) && (this.y == s2.y) && (this.z == s2.z));
	}

	//Not Equals
	public boolean neq(final State other)
	{
		if ( !(other instanceof State3D s2) ) return true;
		return ((this.x != s2.x) || (this.y != s2.y) || (this.z != s2.z));
	}

	@Override
	public List<State> getSuccessors() {
		LinkedList<State> s = new LinkedList<State>();
		State tempState;

		//Generate the successors, starting at the immediate right,
		//Moving in a clockwise manner
		tempState = new State3D(this.x + 1, this.y, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);

		//WARNING: movements can only sum up to 1 square distance
		tempState = new State3D(this.x + 1, this.y + 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y + 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y + 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y + 1 , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y + 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y + 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y + 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y + 1  , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);

		tempState = new State3D(this.x + 1, this.y - 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y - 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y - 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y - 1 , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y - 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y - 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y - 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y - 1  , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);


		return s;
	}

	@Override
	public List<State> getPredecessors() {
		LinkedList<State> s = new LinkedList<State>();
		State tempState;

		//Generate the predecessors, starting at the immediate right,
		//Moving in a clockwise manner
		tempState = new State3D(this.x + 1, this.y, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);

		//WARNING: movements can only sum up to 1 square distance
		tempState = new State3D(this.x + 1, this.y + 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y + 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y + 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y + 1 , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y + 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y + 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y + 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y + 1  , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);

		tempState = new State3D(this.x + 1, this.y - 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y - 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y - 1, this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y - 1 , this.z+ 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y - 1, this.z, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x - 1, this.y - 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x, this.y - 1, this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);
		tempState = new State3D(this.x + 1, this.y - 1  , this.z- 1, new Pair<>(-1.0,-1.0));
		s.addFirst(tempState);

		return s;
	}

	@Override
	public double distanceFrom(State other) {
		if(!(other instanceof State3D state))
			return Double.POSITIVE_INFINITY;
		return Geometry3D.trueDist(this, state);
	}

	@Override
	public double approxDistanceFrom(State other) {
		if(!(other instanceof State3D state))
			return Double.POSITIVE_INFINITY;
		return Geometry3D.twentyFourCondist(this, state);
	}

	//Override the CompareTo function for the HashMap usage
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object aThat) {
		//check for self-comparison
		if ( this == aThat ) return true;

		//use instanceof instead of getClass here for two reasons
		//1. if need be, it can match any supertype, and not just one class;
		//2. it renders an explict check for "that == null" redundant, since
		//it does the check for null already - "null instanceof [type]" always
		//returns false. (See Effective Java by Joshua Bloch.)
		if ( !(aThat instanceof State3D that) ) return false;
		//Alternative to the above line :
		//if ( aThat == null || aThat.getClass() != this.getClass() ) return false;

		//now a proper field-by-field evaluation can be made
		if (this.x == that.x && this.y == that.y && this.z == that.z) return true;
		return false;

	}

}