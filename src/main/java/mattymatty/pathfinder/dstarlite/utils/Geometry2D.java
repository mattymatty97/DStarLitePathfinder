package mattymatty.pathfinder.dstarlite.utils;

import mattymatty.pathfinder.dstarlite.state.State2D;

public class Geometry2D {

    private static final double M_SQRT2 = Math.sqrt(2.0);

    /*
     * Returns the 8-way distance between state a and state b
     */
    public static double eightCondist(State2D a, State2D b)
    {
        double temp;
        double min = Math.abs(a.x - b.x);
        double max = Math.abs(a.y - b.y);
        if (min > max)
        {
            temp = min;
            min = max;
            max = temp;
        }
        return ((M_SQRT2-1.0)*min + max);

    }

    /*
     * Euclidean cost between state a and state b
     */
    public static double trueDist(State2D a, State2D b)
    {
        float x = a.x-b.x;
        float y = a.y-b.y;
        return Math.sqrt(x*x + y*y);
    }

}
