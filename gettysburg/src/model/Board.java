package model;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;

import java.util.*;
import java.util.stream.Collectors;

import static gettysburg.common.ArmyID.CONFEDERATE;
import static gettysburg.common.ArmyID.UNION;
import static gettysburg.common.GbgGameStep.CMOVE;
import static gettysburg.common.GbgGameStep.UMOVE;

public class Board implements GbgBoard {
    public Map<Coordinate, Collection<GbgUnit>> units;
    public Collection<GbgUnit> battled, moved, faceChanged, possibleBattles;

    public Board() {
        units = new HashMap<>();
        battled = new HashSet<>();
        possibleBattles = new HashSet<>();
        moved = new HashSet<>();
        faceChanged = new HashSet<>();
    }

    public void moveUnit(Coordinate src, Coordinate dest, GbgUnit unit) {
        removeUnit(src, unit);
        placeUnit(dest, unit);

        //track change
        moved.add(unit);
    }

    public void removeUnit(Coordinate where, GbgUnit unit) {
        Collection<GbgUnit> presentUnits = units.get(where);
        if (presentUnits == null)
            return; //no need for any action

        presentUnits.remove(unit);
        units.put(where, presentUnits);
    }

    public void placeUnit(Coordinate src, Coordinate dest, GbgUnit unit) {
        if (src != null)
            removeUnit(src, unit);

        Collection<GbgUnit> presentUnits = units.get(dest);
        if (presentUnits == null)
            presentUnits = new LinkedList<>();

        //add and save
        presentUnits.add(unit);
        units.put(dest, presentUnits);
    }

    public void placeUnit(Coordinate where, GbgUnit unit) {
        Coordinate src = null;
        //check if unit is already on board and remove from current location
        for (Coordinate key : units.keySet()) {
            if(units.get(key) == null)
                continue; //skip if null
            if (units.get(key).contains(unit)) {
                src = key;
                break;
            }
        }
        placeUnit(src, where, unit);
    }

    public void placeUnits(Coordinate where, Collection<GbgUnit> units) {
        units.forEach(unit -> placeUnit(where, unit));
    }

    public GbgUnit getUnit(GbgUnit unit) {
        for (Collection<GbgUnit> units : this.units.values()) {
            for (GbgUnit u : units)
                if (u.equals(unit))
                    return u;
        }
        return null;
    }

    public Direction getUnitFacing(GbgUnit unit) {
        GbgUnit u = getUnit(unit);
        if (u == null)
            return null;

        return unit.getFacing();
    }

    public void setUnitFacing(GbgUnit unit, Direction facing) {
        GbgUnit u = getUnit(unit);
        if (u == null)
            throw new GbgInvalidActionException("Can't change facing of unit not on board");

        u.setFacing(facing);
        placeUnit(getUnitLocation(u), u);

        //track change
        faceChanged.add(unit);
    }

    public Collection<GbgUnit> getUnitsAt(Coordinate where) {
        Collection<GbgUnit> result = units.get(where);
        if (result == null || result.size() == 0)
            return null;

        return result;
    }

    public Coordinate getUnitLocation(GbgUnit unit) {
        for (Coordinate where : units.keySet()) {
            if (units.get(where) != null && units.get(where).contains(unit))
                return where;
        }

        //todo: find actual exception to throw
        return null;
    }

    public Collection<GbgUnit> getAllUnits() {
        Collection<GbgUnit> result = new HashSet<>();//set to avoid duplication

        for (Collection<GbgUnit> units : this.units.values()) {
            if (units != null)
                result.addAll(units);
        }
        return result;
    }

    public Collection<GbgUnit> getAllAttackingUnits(GbgGameStep step) {
        switch (step) {
            case UMOVE:
            case UBATTLE:
                return getAllUnits().stream().filter(unit -> unit.getArmy() == UNION).collect(Collectors.toSet());
            case CMOVE:
            case CBATTLE:
                return getAllUnits().stream().filter(unit -> unit.getArmy() == CONFEDERATE).collect(Collectors.toSet());
            default:
                return null;
        }
    }

    /**
     * Get all units defending in the current step
     */
    public Collection<GbgUnit> getAllDefendingUnits(GbgGameStep step) {
        Collection<GbgUnit> result = new HashSet<>(getAllUnits());
        result.removeAll(getAllAttackingUnits(step));
        return result;
    }

    public void clear() {
        units = new HashMap<>();
    }


    /**
     * Set all possible battles for this move.
     * note: to be done once per battle move (cbattle/ubattle)
     *
     * @param battles
     */
    public void setAllPossibleBattles(Collection<BattleDescriptor> battles) {
        for (BattleDescriptor battle : battles) {
            possibleBattles.addAll(battle.getAttackers());
            possibleBattles.addAll(battle.getDefenders());
        }
    }


    public Collection<Coordinate> getActiveZonesOfControl(GbgGameStep step) {
        Collection<Coordinate> result = new HashSet<>();
        if (step == UMOVE) {//get confederate zones
            for (Collection<GbgUnit> list : units.values()) {
                if(list == null)
                    continue;

                for (GbgUnit unit : list) {
                    if (unit.getArmy() == UNION)
                        continue;//skip union units
                    result.addAll(GameCoordinate.makeCoordinate(getUnitLocation(unit)).getZoneOfControl(unit));
                }
            }
        } else if (step == CMOVE) {
            for (Collection<GbgUnit> list : units.values()) {
                if(list == null)
                    continue;

                for (GbgUnit unit : list) {
                    if (unit.getArmy() == CONFEDERATE)
                        continue;//skip confederate units
                    result.addAll(GameCoordinate.makeCoordinate(getUnitLocation(unit)).getZoneOfControl(unit));
                }
            }
        }
        return result;
    }

    //todo: add reset

    public void removeAllStacks() {
        Collection<Coordinate> toRemove = units.keySet().stream()
                .filter(key -> {
                    Collection<GbgUnit> squares = units.get(key);
                    if (squares != null && squares.size() > 1)
                        return true;
                    return false;
                }).collect(Collectors.toSet());

        for (Coordinate where : toRemove) {
            units.put(where, null);
        }
    }

    public void applyResolution(BattleResolution resolution){
        removeUnits(resolution.getEliminatedConfederateUnits());
        removeUnits(resolution.getEliminatedUnionUnits());

        //add all participants to battled
        battled.addAll(resolution.getEliminatedConfederateUnits());
        battled.addAll(resolution.getEliminatedUnionUnits());
        battled.addAll(resolution.getActiveConfederateUnits());
        battled.addAll(resolution.getActiveUnionUnits());
    }

    public void removeUnits(Collection<GbgUnit> units){
        for(GbgUnit unit : units){
            removeUnit(getUnitLocation(unit), unit);
        }
    }
}
