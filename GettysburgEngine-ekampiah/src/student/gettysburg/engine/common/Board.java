package student.gettysburg.engine.common;

import java.util.*;
import java.util.stream.Collectors;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgBoard;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import student.gettysburg.engine.utility.configure.UnitInitializer;

public class Board implements GbgBoard {
    private Map<Coordinate, Collection<GbgUnit>> board;

    public Board(List<UnitInitializer> config) {
        board = new HashMap<>();
        config.forEach(unit -> {
            addUnit(unit.where, unit.unit);
        });
    }

    public void move(GbgUnit unit, Coordinate from, Coordinate to) {
        removeUnit(from, unit);
        addUnit(to, unit);
    }

    public void addUnit(Coordinate where, GbgUnit unit) {
        Collection<GbgUnit> previous = board.get(where);
        if (previous == null)
            previous = new HashSet<>();

        if (previous.contains(unit))
            previous.remove(unit);

        previous.add(unit);
        board.put(where, previous);
    }

    private void removeUnit(Coordinate where, GbgUnit unit) {
        Collection<GbgUnit> units = board.get(where);
        if (units != null && units.contains(unit))
            units.remove(unit);
    }

    public Collection<GbgUnit> getUnitsAt(Coordinate where) {
        Collection<GbgUnit> units = board.get(where);
        return units == null || units.isEmpty() ? null : units;
    }

    public Coordinate getUnitLocation(GbgUnit unit) {
        for (Coordinate coord : board.keySet()) {
            if (board.get(coord).contains(unit))
                return coord;
        }

        return null;
    }

    public GbgUnit findUnit(GbgUnit unit) {
        for (Collection<GbgUnit> set : board.values()) {
            if (set.contains(unit)) {
                List<GbgUnit> helper = set.stream().collect(Collectors.toList());
                return helper.get(helper.indexOf(unit));
            }
        }
        return null;
    }

    /**
     * After each turn, use this method to reset tracking of changed direction
     */
    public void resetFacingChanged() {
        board.values().forEach(set -> {
            set.forEach(unit -> ((GbgUnitImpl) unit).setFacingChanged(false));
        });
    }

    public static Collection<Coordinate> getAdjacentSquares(Coordinate where) {
        List<Coordinate> coordinates = new LinkedList<>();
        //shift left
        try {
            Coordinate W = CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY());
            coordinates.add(W);
        } catch (GbgInvalidCoordinateException ex) {

        }
        try {
            Coordinate NW = CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1);
            coordinates.add(NW);
        } catch (GbgInvalidCoordinateException ex) {

        }
        try {
            Coordinate SW = CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1);
            coordinates.add(SW);
        } catch (GbgInvalidCoordinateException ex) {

        }

        //shift right
        try {
            Coordinate E = CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY());
            coordinates.add(E);
        } catch (GbgInvalidCoordinateException ex) {

        }

        try {
            Coordinate NE = CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1);
            coordinates.add(NE);
        } catch (GbgInvalidCoordinateException ex) {

        }

        try {
            Coordinate SE = CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1);
            coordinates.add(SE);
        } catch (GbgInvalidCoordinateException ex) {

        }
        //middle
        try {
            Coordinate N = CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1);
            coordinates.add(N);
        } catch (GbgInvalidCoordinateException ex) {

        }
        try {
            Coordinate S = CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1);
            coordinates.add(S);
        } catch (GbgInvalidCoordinateException ex) {

        }


        return coordinates;
    }

}
