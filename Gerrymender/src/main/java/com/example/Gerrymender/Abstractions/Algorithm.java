package com.example.Gerrymender.Abstractions;


public class Algorithm {
    /*objectiveValue -> calculating the objective function for each iteration
        numIterations -> For simulated annealing
     */
    public int objectiveValue;
    public int numIterations;

    public boolean combine(Cluster c) { //TODO
        return true; //Combines clusters
    }


    public void annealling() {
        //TODO
    }

//    public boolean addPrecinct(State s, Precinct p) {
//        return s.getPrecincts().add(p);
//    }


    public boolean addEdge(Cluster c1, Cluster c2) {

        //return c1.getEdges().add(new Edge());
        return true;

    }
}
