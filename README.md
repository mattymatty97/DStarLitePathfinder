DStarLiteJava
=============

A java implementation of the incremental heuristic search algorithm D* Lite.

Getting Started
=============

Import the DStarLite, Pair and State files into your project.

Example usage: 

    //Create pathfinder
      
    CostProvider costProvider = new CellCostProvider(1);
    DStarLite pf = new DStarLite(State3D.class, costProvider);
    State start = new State3D(0,0,1);
    State goal = new State3D(3,0,1);
    pf.init(start,goal);
    pf.updateCell(new State3D(2,0,1), -1);
    pf.updateCell(new State3D(2, 0,0), -1);
    pf.updateCell(new State3D(2, 0,2), -1);
    pf.updateCell(new State3D(3, 0,0), -1);

    System.out.println("Start node: (0,0,1)");
    System.out.println("End node: (3,0,1)");

    //Time the replanning
    long begin = System.currentTimeMillis();
    pf.replan();
    long end = System.currentTimeMillis();

    System.out.println("Time: " + (end-begin) + "ms");

    List<State> path = pf.getPath();
    for (State i : path)
    {
        State3D p = (State3D)i;
        System.out.println("x: " + p.x + " y: " + p.y + " z: " + p.z);
    }
      
License
=============
MIT