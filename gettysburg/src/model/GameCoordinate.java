package model;

import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import util.BFS;
import validators.LocationValidators;

import java.util.LinkedList;
import java.util.List;

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

    public List<Coordinate> getNeighbors() {
        List<Coordinate> neighbors = new LinkedList<>();
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

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if (this == o) return true;
        if(!(o instanceof Coordinate)) return false;

        GameCoordinate that = (GameCoordinate) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
