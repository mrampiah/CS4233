package model;

import gettysburg.common.*;
import util.UnitInitializer;

public class Unit implements GbgUnit {
    private ArmyID armyID;
    private int combatFactor, movementFactor;
    private Direction facing;
    private String leader;
    private UnitSize unitSize;
    private UnitType unitType;

    /**
     * Default constructor. Initialize all values to default
     */
    public Unit() {
        this(null, 0, 0, null, null, null, null);
    }

    /**
     * Constructor that requires all information for a unit
     * @param armyID
     * @param combatFactor
     * @param movementFactor
     * @param facing
     * @param leader
     * @param unitSize
     * @param unitType
     */
    public Unit(ArmyID armyID, int combatFactor, int movementFactor, Direction facing, String leader,
                UnitSize unitSize, UnitType unitType) {
        this.armyID = armyID;
        this.combatFactor = combatFactor;
        this.movementFactor = movementFactor;
        this.facing = facing;
        this.leader = leader;
        this.unitSize = unitSize;
        this.unitType = unitType;
    }


    /**
     * Factory method to create a unit that can be compared
     *
     * @param armyID
     * @param leader
     */
    public static Unit makeBasicUnit(ArmyID armyID, String leader) {
        return new Unit(armyID, 0, 0, null, leader, null, null);
    }

    public static Unit makeUnit(ArmyID armyID, int combatFactor, int movementFactor, Direction facing, String leader,
                                UnitSize unitSize, UnitType unitType){
        return new Unit(armyID, combatFactor, movementFactor, facing, leader, unitSize, unitType);
    }

    public static Unit makeUnit(GbgUnit unit){
        return new Unit(unit.getArmy(), unit.getCombatFactor(), unit.getMovementFactor(), unit.getFacing(),
                unit.getLeader(), unit.getSize(), unit.getType());
    }

    public static Unit makeUnit(UnitInitializer init){
        return new Unit(init.unit.getArmy(), init.unit.getCombatFactor(), init.unit.getMovementFactor(), init.unit.getFacing(),
                init.unit.getLeader(), init.unit.getSize(), init.unit.getType());
    }

    @Override
    public UnitType getType() {
        return unitType;
    }

    @Override
    public int getCombatFactor() {
        return combatFactor;
    }

    @Override
    public int getMovementFactor() {
        return movementFactor;
    }

    @Override
    public ArmyID getArmy() {
        return armyID;
    }

    @Override
    public UnitSize getSize() {
        return unitSize;
    }

    @Override
    public String getLeader() {
        return leader;
    }

    @Override
    public Direction getFacing() {
        return facing;
    }

    @Override
    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Unit)) return false;

        Unit unit = (Unit) o;

        if (armyID != unit.armyID) return false;
        return leader != null ? leader.equals(unit.leader) : unit.leader == null;
    }

    @Override
    public int hashCode() {
        int result = armyID != null ? armyID.hashCode() : 0;
        result = 31 * result + (leader != null ? leader.hashCode() : 0);
        return result;
    }
}
