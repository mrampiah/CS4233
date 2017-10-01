/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package student.gettysburg.engine.common;

import java.util.*;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import gettysburg.common.exceptions.GbgNotImplementedException;
import student.gettysburg.engine.exceptions.UnitNotFoundException;
import student.gettysburg.engine.utility.configure.UnitInitializer;

/**
 * This is the game engine master class that provides the interface to the game
 * implementation. DO NOT change the name of this file and do not change the
 * name ofthe methods that are defined here since they must be defined to
 * implement the GbgGame interface.
 *
 * @version Jun 9, 2017
 */
public class GettysburgEngine implements GbgGame {
    // private Map<Coordinate, GbgUnit> board;
    private Board board;
    private int turn = 0;
    private GbgGameStep step;
    private Set<GbgUnit> movedUnits;

    public GettysburgEngine(String version, List<UnitInitializer> config) {
        // board = new HashMap<Coordinate, GbgUnit>(units);
        board = new Board(config);

        turn = 1;
        step = GbgGameStep.UMOVE; // start on union move

        //initialized moved units collection
        movedUnits = new HashSet<>();
    }

    /*
     * @see gettysburg.common.GbgGame#endBattleStep()
     */
    @Override
    public void endBattleStep() {
        //check if on move
        if (step == GbgGameStep.UMOVE || step == GbgGameStep.CMOVE)
            throw new RuntimeException("Invalid move");

        if (step == GbgGameStep.UBATTLE)
            step = GbgGameStep.CMOVE;

        //todo: check end game conditions, etc
        //but for now..
        if (step == GbgGameStep.CBATTLE) {
            turn++;
            step = GbgGameStep.GAME_OVER; //for alpha
            //fresh start for each turn
            movedUnits.clear();
        }
    }

    /*
     * @see gettysburg.common.GbgGame#endMoveStep()
     */
    @Override
    public void endMoveStep() {
        //check if on battle
        if (step == GbgGameStep.UBATTLE || step == GbgGameStep.CBATTLE)
            throw new RuntimeException("Invalid move");

        //change to appropriate battle
        if (step == GbgGameStep.UMOVE)
            step = GbgGameStep.UBATTLE;
        else if (step == GbgGameStep.CMOVE)
            step = GbgGameStep.CBATTLE;

        //reset facing after a move
        board.resetFacingChanged();
    }

    /*
     * @see gettysburg.common.GbgGame#endStep()
     */
    @Override
    public GbgGameStep endStep() {
        switch (step) {
            case CBATTLE:
            case UBATTLE:
                endBattleStep();
                break;
            case CMOVE:
            case UMOVE:
                endMoveStep();
                break;
            case GAME_OVER:
                break;
            default:
                break;
        }
        return step;
    }

    /*
     * @see gettysburg.common.GbgGame#getBattlesToResolve()
     */
    @Override
    public Collection<BattleDescriptor> getBattlesToResolve() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see gettysburg.common.GbgGame#getCurrentStep()
     */
    @Override
    public GbgGameStep getCurrentStep() {
        return step;
    }

    /*
     * @see gettysburg.common.GbgGame#getGameStatus()
     */
    @Override
    public GbgGameStatus getGameStatus() {
        return turn > 1? GbgGameStatus.UNION_WINS : GbgGameStatus.IN_PROGRESS;
    }

    /*
     * @see gettysburg.common.GbgGame#getGameDate()
     */
    @Override
    public Calendar getGameDate() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see
     * gettysburg.common.GbgGame#getSquareDescriptor(gettysburg.common.Coordinate)
     */
    @Override
    public GbgSquareDescriptor getSquareDescriptor(Coordinate where) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see gettysburg.common.GbgGame#getTurnNumber()
     */
    @Override
    public int getTurnNumber() {
        return turn;
    }

    /*
     * @see gettysburg.common.GbgGame#getUnitFacing(int)
     */
    @Override
    public Direction getUnitFacing(GbgUnit unit) {
        GbgUnit result = board.findUnit(unit);

        if(result == null)
            throw new UnitNotFoundException();
        else
            return result.getFacing();
    }

    /*
     * @see gettysburg.common.GbgGame#getUnitsAt(gettysburg.common.Coordinate)
     */
    @Override
    public Collection<GbgUnit> getUnitsAt(Coordinate where) {
        return board.getUnitsAt(where);
    }

    /*
     * @see gettysburg.common.GbgGame#moveUnit(gettysburg.common.GbgUnit,
     * gettysburg.common.Coordinate, gettysburg.common.Coordinate)
     */
    @Override
    public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to) {
        if(movedUnits.contains(unit))
            throw new GbgInvalidMoveException("Already moved unit");
        if (validMove(unit, from, to)) {
            board.move(unit, from, to);
            movedUnits.add(unit);
        }else
            throw new GbgInvalidMoveException(
                    String.format("Unable to move %s to (%d, %d)", unit.getLeader(), to.getX(), to.getY()));
    }

    private boolean validMove(GbgUnit unit, Coordinate from, Coordinate to) {
        boolean destinationOnBoard = to.getX() <= board.COLUMNS && to.getY() <= board.ROWS;
        boolean notEqual = !from.equals(to);
        boolean validDistance = unit.getMovementFactor() >= from.distanceTo(to);
        boolean noUnitsAtSource = noUnitsPresent(from);
        boolean noUnitsAtDest = noUnitsPresent(to);
        boolean matchingUnit = (unit.getArmy() == ArmyID.UNION && step == GbgGameStep.UMOVE)
                || (unit.getArmy() == ArmyID.CONFEDERATE && step == GbgGameStep.CMOVE);

        return destinationOnBoard && notEqual && validDistance && !noUnitsAtSource && noUnitsAtDest && matchingUnit;
    }

    private boolean noUnitsPresent(Coordinate coord) {
        try {
            return getUnitsAt(coord).isEmpty();
        } catch (NullPointerException e) {
            return true;
        }
    }

    /*
     * @see gettysburg.common.GbgGame#resolveBattle(int)
     */
    @Override
    public BattleResolution resolveBattle(BattleDescriptor battle) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see gettysburg.common.GbgGame#setUnitFacing(gettysburg.common.GbgUnit,
     * gettysburg.common.Direction)
     */
    @Override
    public void setUnitFacing(GbgUnit unit, Direction direction) {
        if(((GbgUnitImpl) unit).isFacingChanged())
            throw new GbgInvalidMoveException("Can't change facing twice");

        if(step == GbgGameStep.CBATTLE || step == GbgGameStep.UBATTLE)
            throw new GbgInvalidMoveException("Wrong time to change facing");

        if( (unit.getArmy() == ArmyID.UNION && step == GbgGameStep.CMOVE) ||
                unit.getArmy() == ArmyID.CONFEDERATE && step == GbgGameStep.UMOVE)
            throw new GbgInvalidMoveException("Not your turn");

        unit.setFacing(direction);
        board.addUnit(whereIsUnit(unit), unit);
    }

    /*
     * @see gettysburg.common.GbgGame#whereIsUnit(gettysburg.common.GbgUnit)
     */
    @Override
    public Coordinate whereIsUnit(GbgUnit unit) {
        return board.getUnitLocation(unit);
    }

    /*
     * @see gettysburg.common.GbgGame#whereIsUnit(java.lang.String,
     * gettysburg.common.ArmyID)
     */
    @Override
    public Coordinate whereIsUnit(String leader, ArmyID army) {
        return whereIsUnit(new GbgUnitImpl(leader, army));
    }

    public Board getBoard() {
        return board;
    }
}
