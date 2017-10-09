package util;

import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGame;
import model.GameCoordinate;

import java.util.*;
import java.util.stream.Collectors;

public class BFS {
    private static Map<Coordinate, Integer> distance;
    private static Set<Coordinate> settled, unsettled;
    private static Map<Coordinate, Coordinate> predecessors;
    private static final int EDGE_COST = 1;
    private static Coordinate src;

    public static void evaluate(Coordinate src){
        settled = new HashSet<>();
        unsettled = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        BFS.src = src;

        distance.put(src, 0);
        unsettled.add(src);

        while(unsettled.size() > 0){
            Coordinate current = getMinimalDistance();
            settled.add(current);
            unsettled.remove(current);

            //evaluate neighbors
            evaluateNeighbors(current);
        }
    }

    private static Coordinate getMinimalDistance(){
        //get first coordinate as base
        Coordinate winner = null;

        for(Coordinate coord : unsettled){
            if(winner == null)
                winner = coord;
            else{
                if(getShortestDistance(coord) < getShortestDistance(winner))
                    winner = coord;
            }
        }

        return winner;
    }

    private static void evaluateNeighbors(Coordinate coord){
        Collection<Coordinate> neighbors = GameCoordinate.makeCoordinate(coord).getNeighbors();

        for(Coordinate coordinate : neighbors.stream()
                .filter(c -> !settled.contains(c)).collect(Collectors.toList())){

//            int edgeDistance = settled.get(coord) + EDGE_COST;
            int newDistance = getShortestDistance(coord) + EDGE_COST;

            if(getShortestDistance(coordinate) > newDistance){
                distance.put(coordinate, newDistance);
                predecessors.put(coordinate, coord);
                unsettled.add(coordinate);
            }
        }
    }

    private static int getShortestDistance(Coordinate coord){
        Integer d = distance.get(coord);
        if(d == null)
            return Integer.MAX_VALUE;

        return d;
    }

    public static List<Coordinate> shortestPath(Coordinate coord){
        List<Coordinate> path = new LinkedList<>();
        Coordinate step = coord;
        if(predecessors.get(step) == null && !step.equals(src)){
            return null;
        }
        path.add(step);
        while(predecessors.get(step) != null){
            step = predecessors.get(step);
            path.add(step);
        }

        Collections.reverse(path);
        return path;
    }
}