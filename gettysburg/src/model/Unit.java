package model;

import gettysburg.common.*;

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
}
