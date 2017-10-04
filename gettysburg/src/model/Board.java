package model;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgBoard;
import gettysburg.common.GbgUnit;

import java.util.*;

public class Board implements GbgBoard {
    private Map<Coordinate, Collection<GbgUnit>> units;

    public Board(){
        units = new HashMap<>();
    }

    public boolean isCoordinateValid(Coordinate where){
        boolean validColumn = 0 > where.getX() || where.getX() <= GbgBoard.COLUMNS;
        boolean validRow = 0 > where.getY() || where.getY() <= GbgBoard.ROWS;

        return validColumn && validRow;
    }

    public void moveUnit(Coordinate src, Coordinate dest, Unit unit){
        removeUnit(src, unit);
        placeUnit(dest, unit);
    }

    public void removeUnit(Coordinate where, Unit unit){
        Collection<GbgUnit> presentUnits = units.get(where);
        if(presentUnits == null)
            return; //no need for any action

        presentUnits.remove(unit);
        units.put(where, presentUnits);
    }

    public void placeUnit(Coordinate where, GbgUnit unit){
        Collection<GbgUnit> presentUnits = units.get(where);
        if(presentUnits == null)
            presentUnits = new LinkedList<>();

        //add and save
        presentUnits.add(unit);
        units.put(where, presentUnits);
    }

    public Collection<GbgUnit> getUnitsAt(Coordinate where){
        return units.get(where);
    }
}
