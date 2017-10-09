package model;

import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import util.BFS;
import validators.LocationValidators;

import java.util.*;

public class GameCoordinate implements Coordinate {
    private int x, y;

    private GameCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static GameCoordinate makeCoordinate(int x, int y) {
        GameCoordinate where = new GameCoordinate(x, y);
        if (LocationValidators.locationOnBoardValidator.apply(where))
            return where;
        else
            throw new GbgInvalidCoordinateException("Can't create coordinate (" + x + ", "
                    + y + ")");
    }

    public static GameCoordinate makeCoordinate(Coordinate where) {
        GameCoordinate newCoord = new GameCoordinate(where.getX(), where.getY());
        if (LocationValidators.locationOnBoardValidator.apply(where))
            return newCoord;
        else
            throw new GbgInvalidCoordinateException("Can't create coordinate (" +
                    where.getX() + ", " + where.getY() + ")");
    }

    @Override
    public int distanceTo(Coordinate dest) {
        BFS.evaluate(this);
        //account for current square which is included in result
        List<Coordinate> result = BFS.shortestPath(dest);
        if (result == null)
            throw new GbgInvalidMoveException("no valid paths");
        return result.size() - 1;
    }

    /*
     * @see gettysburg.common.Coordinate#directionTo(gettysburg.common.Coordinate)
	 */
    @Override
    public Direction directionTo(Coordinate coordinate) {
        if (y == coordinate.getY() && x == coordinate.getX())
            return Direction.NONE;

        boolean north = y > coordinate.getY();
        boolean south = y < coordinate.getY();
        boolean east = x < coordinate.getX();
        boolean west = x > coordinate.getX();

        if (north) {
            if (x == coordinate.getX())
                return Direction.NORTH;
            if (north && east)
                return Direction.NORTHEAST;
            if (north && west)
                return Direction.NORTHWEST;
        } else if (south) {
            if (x == coordinate.getX())
                return Direction.SOUTH;

            if (south && east)
                return Direction.SOUTHEAST;
            if (south && west)
                return Direction.SOUTHWEST;
        } else {
            if (east)
                return Direction.EAST;
            if (west)
                return Direction.WEST;
        }

        return Direction.NONE;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public Collection<Coordinate> getNeighbors() {
        Collection<Coordinate> neighbors = new HashSet<>();
        //shift left
        try {
            Coordinate W = GameCoordinate.makeCoordinate(x - 1, y);
            neighbors.add(W);
        } catch (GbgInvalidCoordinateException ex) {

        }
        try {
            Coordinate NW = GameCoordinate.makeCoordinate(x - 1, y + 1);
            neighbors.add(NW);
        } catch (GbgInvalidCoordinateException ex) {

        }
        try {
            Coordinate SW = GameCoordinate.makeCoordinate(x - 1, y - 1);
            neighbors.add(SW);
        } catch (GbgInvalidCoordinateException ex) {

        }

        //shift right
        try {
            Coordinate E = GameCoordinate.makeCoordinate(x + 1, y);
            neighbors.add(E);
        } catch (GbgInvalidCoordinateException ex) {

        }

        try {
            Coordinate NE = GameCoordinate.makeCoordinate(x + 1, y + 1);
            neighbors.add(NE);
        } catch (GbgInvalidCoordinateException ex) {

        }

        try {
            Coordinate SE = GameCoordinate.makeCoordinate(x + 1, y - 1);
            neighbors.add(SE);
        } catch (GbgInvalidCoordinateException ex) {

        }
        //middle
        try {
            Coordinate N = GameCoordinate.makeCoordinate(x, y + 1);
            neighbors.add(N);
        } catch (GbgInvalidCoordinateException ex) {

        }
        try {
            Coordinate S = GameCoordinate.makeCoordinate(x, y - 1);
            neighbors.add(S);
        } catch (GbgInvalidCoordinateException ex) {

        }
        return neighbors;
    }

    public Collection<Coordinate> getZoneOfControl(GbgUnit unit){
        GameCoordinate N = null, NW = null, NE = null, E = null, SE = null, S = null, SW = null, W = null;
        try{
             NW = GameCoordinate.makeCoordinate(x - 1, y - 1);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            N = GameCoordinate.makeCoordinate(x, y - 1);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            NE = GameCoordinate.makeCoordinate(x + 1, y - 1);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            E = GameCoordinate.makeCoordinate(x + 1, y);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            SE = GameCoordinate.makeCoordinate(x + 1, y + 1);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            S = GameCoordinate.makeCoordinate(x , y + 1);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            SW = GameCoordinate.makeCoordinate(x - 1, y + 1);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }
        try{
            W = GameCoordinate.makeCoordinate(x - 1, y);
        }catch (GbgInvalidCoordinateException ex){
            //suppress
        }

        Collection<Coordinate> active = new HashSet<>();
        switch (unit.getFacing()) {
            case NORTH:
                if(NW != null)
                    active.add(NW);

                if(N != null)
                    active.add(N);

                if(NE != null)
                    active.add(NE);

                return active;
            case NORTHEAST:
                if(NE != null)
                active.add(NE);

                if(N != null)
                    active.add(N);

                if(E != null)
                    active.add(E);

                return active;
            case EAST:
                if(E != null)
                    active.add(E);

                if(NE != null)
                    active.add(NE);

                if(SE != null)
                    active.add(SE);

                return active;
            case SOUTHEAST:
                if(E != null)
                active.add(E);

                if(S != null)
                    active.add(S);

                if(SE != null)
                    active.add(SE);

                return active;
            case SOUTH:
                if(SW != null)
                active.add(SW);

                if(S != null)
                    active.add(S);

                if(SE != null)
                    active.add(SE);

                return active;
            case SOUTHWEST:
                if(SW != null)
                    active.add(SW);

                if(S != null)
                    active.add(S);

                if(W != null)
                    active.add(W);

                return active;
            case WEST:
                if(SW != null)
                    active.add(SW);

                if(NW != null)
                    active.add(NW);

                if(W != null)
                    active.add(W);

                return active;
            case NORTHWEST:
                if(N != null)
                    active.add(N);

                if(NW != null)
                    active.add(NW);

                if(W != null)
                    active.add(W);

                return active;
            case NONE:
                break;
        }

        return null;
    }


    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if (this == o) return true;
        if(!(o instanceof Coordinate)) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.getX()) return false;
        return y == that.getY();
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
