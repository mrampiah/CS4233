package student.gettysburg.engine.common;

import java.util.*;
import java.util.stream.Collectors;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import student.gettysburg.engine.utility.configure.UnitInitializer;

public class Board implements GbgBoard {
    private Map<Coordinate, Collection<GbgUnit>> board;

    public Board() {
        board = new HashMap<>();
    }

    public void move(GbgUnit unit, Coordinate from, Coordinate to) {
            tryAddUnit(to, unit);
        removeUnit(from, unit);
    }


    /*
     * no units at src(where)
     *
     */
    public void addUnit(Coordinate where, GbgUnit unit) {
        Collection<GbgUnit> previous = board.get(where);
        if (previous == null)
            previous = new HashSet<>();

        if (previous.contains(unit))
            previous.remove(unit);

        previous.add(unit);
        board.put(where, previous);
    }

    public void tryAddUnit(Coordinate where, GbgUnit unit) {
        if (RuleEngine.getRuleEngine().validatePlacement(where, unit, false))
            board.put(where, Arrays.asList(unit));
        else
            throw new GbgInvalidMoveException("Can't add unit");
    }

    public void setUnit(Coordinate where, GbgUnit unit) {
        if (RuleEngine.getRuleEngine().validatePlacement(where, unit, true))
            board.put(where, Arrays.asList(unit));
        else
            throw new GbgInvalidMoveException("Can't add unit");
    }

    //for stacking at start of game
    public void addUnits(List<UnitInitializer> config, boolean start) {
        config.forEach(unit -> {
            if (start) //enable stack only at start
                addUnit(unit.where, unit.unit);
            else
                tryAddUnit(unit.where, unit.unit);
        });
    }

    private void removeUnit(Coordinate where, GbgUnit unit) {
        if(board.get(where) == null)
            throw new GbgInvalidMoveException("nothing to remove");

        Collection<GbgUnit> units = new LinkedList(board.get(where));
        if (units.contains(unit))
            units.remove(unit);

        if(units.size() == 0)
            board.put(where, null);
        else
            board.put(where, units);
    }

    public Collection<GbgUnit> getUnitsAt(Coordinate where) {
        Collection<GbgUnit> units = board.get(where);
        return units == null || units.isEmpty() ? null : units;
    }

    public Coordinate getUnitLocation(GbgUnit unit) {
        for (Coordinate coord : board.keySet()) {
            if (board.get(coord) != null && board.get(coord).contains(unit))
                return coord;
        }

        return null;
    }

    public GbgUnit findUnit(GbgUnit unit) {
        for (Collection<GbgUnit> set : board.values()) {
            if (set != null && set.contains(unit)) {
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
        try {
            board.values().forEach(set -> {
                set.forEach(unit -> ((GbgUnitImpl) unit).setFacingChanged(false));
            });
        }catch (NullPointerException e){

        }
    }

    public static List<Coordinate> getAdjacentSquares(Coordinate where) {
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

    public Collection<Coordinate> getZoneOfControl(GbgUnit unit) {
        Coordinate where = getUnitLocation(unit);
        switch (unit.getFacing()) {
            case NORTH:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1),//NW
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1), //N
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1)); //NE
            case NORTHEAST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1), //NE
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1), //N
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY())); //e
            case EAST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY()), //E
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1), //NE
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1)); //SE
            case SOUTHEAST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY()), //E
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1), //S
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1)); //SE
            case SOUTH:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1), //SW
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1), //S
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1)); //SE
            case SOUTHWEST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1), //SW
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1), //S
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY())); //W
            case WEST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1), //SW
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1), //NW
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY())); //W
            case NORTHWEST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1), //N
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1), //NW
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY())); //W
            case NONE:
                break;
        }

        throw new IllegalArgumentException("Unable to get zone of control");
    }

    /**
     * need to remove all stacks on board at the end of every move step
     */
    public void removeStacks(){
        Set<Coordinate> keys =  new HashSet<>(board.keySet());
        for(Coordinate key : keys){
            if(board.get(key).size() > 1)
                board.remove(key);
        }
    }

    public List<GbgUnit> getAllUnits(ArmyID army){
        List<GbgUnit> result = new LinkedList<>();
        for(Collection<GbgUnit> units : board.values())
            result.addAll(units.stream().filter(unit -> unit.getArmy() == army).collect(Collectors.toList()));

        return result;
    }

    public void clear(){
        board.clear();
    }
}
