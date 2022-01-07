package mattymatty.pathfinder.dstarlite.utils;

import mattymatty.pathfinder.dstarlite.state.State3D;

public class Geometry3D {

    private static final double M_SQRT2 = Math.sqrt(2.0);
    private static final double M_SQRT3 = Math.sqrt(3.0);

    /*
     * Returns the 26-way distance between state a and state b
     */
    public static double twentyFourCondist(State3D a, State3D b)
    {
        double dx = Math.abs(a.x - b.x);
        double dy = Math.abs(a.y - b.y);
        double dz = Math.abs(a.z - b.z);
        double min = Math.min(Math.min(dx,dy),dy);
        double max = Math.max(Math.max(dx,dy),dy);
        double mid = dx + dy + dz - min - max;
        return (M_SQRT3 - M_SQRT2) * min + (M_SQRT2 - 1.0) * mid + max;
    }

    /*
     * Euclidean cost between state a and state b
     */
    public static double trueDist(State3D a, State3D b)
    {
        float x = a.x-b.x;
        float y = a.y-b.y;
        float z = a.z-b.z;
        return Math.sqrt(x*x + y*y + z*z);
    }

}
