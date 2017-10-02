package student.gettysburg.engine.common;

import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgBoard;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import student.gettysburg.engine.utility.configure.UnitInitializer;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static gettysburg.common.Direction.*;

public class GBoard implements GbgBoard{
    private Coordinate[][] board;
    private Map<Coordinate, List<GbgUnit>> units;

    public GBoard() {
        board = new Coordinate[GbgBoard.ROWS][GbgBoard.COLUMNS];
        units = new HashMap<>();
    }

    public GBoard(List<UnitInitializer> config) {
        this();

        config.forEach(unit -> {
            addUnit(unit.where, unit.unit);
        });
    }

    public void addUnit(Coordinate where, GbgUnit unit) {
        List<GbgUnit> previous = units.get(where);
        if (previous == null)
            previous = new LinkedList<>();

        if (previous.contains(unit))
            previous.remove(unit);

        previous.add(unit);
        units.put(where, previous);
    }

    private void removeUnit(Coordinate where, GbgUnit unit) {
        List<GbgUnit> current = units.get(where);
        if (current != null && current.contains(unit)) {
            current.remove(unit);
            units.put(where, current);//save deletion
        }
    }

    public static List<Coordinate> bfs(Coordinate start, Coordinate end){
        Queue<Coordinate> queue = new LinkedBlockingQueue<>();
        List<Coordinate>  visited = new LinkedList<>();
        Direction direction = start.directionTo(end);

        queue.add(start);
        visited.add(start);

        while(!queue.isEmpty()){
            System.out.println(direction);
            Coordinate top = queue.poll();
            if(visited.contains(end)){
                break;
            }
            List<Coordinate> neighbors = new LinkedList<>();
            Coordinate above = null, below = null, left = null, right = null;

            try{
                above = CoordinateImpl.makeCoordinate(top.getX(), top.getY() - 1);
                neighbors.add(above);
            }catch(GbgInvalidCoordinateException ex){

            }

            try{
                below = CoordinateImpl.makeCoordinate(top.getX(), top.getY() + 1);
                neighbors.add(below);
            }catch(GbgInvalidCoordinateException ex){

            }

            try{
                left = CoordinateImpl.makeCoordinate(top.getX() - 1, top.getY());
                neighbors.add(left);
            }catch(GbgInvalidCoordinateException ex){

            }

            try{
                right = CoordinateImpl.makeCoordinate(top.getX() + 1, top.getY());
                neighbors.add(right);
            }catch(GbgInvalidCoordinateException ex){

            }

            switch(direction){
                case NORTHEAST:
                    addNeighbor(neighbors, right);
                case NORTH:
                    addNeighbor(neighbors, above);
                    break;

                    ///room
                case EAST:
                    addNeighbor(neighbors, right);
                    break;
                case WEST:
                    addNeighbor(neighbors, left);
                    break;

                case SOUTHEAST:
                    addNeighbor(neighbors, right);
                case SOUTH:
                    addNeighbor(neighbors, below);
                    break;
                case SOUTHWEST:
                    addNeighbor(neighbors, below);
                    addNeighbor(neighbors, left);
                    break;
                case NORTHWEST:
                    addNeighbor(neighbors, above);
                    addNeighbor(neighbors, left);
                    break;
                case NONE:
                    break;
            }

            neighbors.forEach(c -> System.out.printf("%s ", c));
            for(Coordinate coord : neighbors){
                if(!visited.contains(coord)){
                    visited.add(coord);
                    queue.add(coord);
                }
            }
        }
        return visited;
    }

    private static void addNeighbor(List<Coordinate> neighbors, Coordinate coord){
        if(coord != null && !neighbors.contains(coord))
            neighbors.add(coord);
    }

}
