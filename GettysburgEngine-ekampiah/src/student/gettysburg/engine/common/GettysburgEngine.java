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
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sun.javaws.exceptions.InvalidArgumentException;
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


    public Collection<Coordinate> getZoneOfControl(GbgUnit unit){
        Coordinate where = board.getUnitLocation(unit);
        switch(unit.getFacing()){
            case NORTH:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1),//NW
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1), //N
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1)); //NE
            case NORTHEAST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1), //NE
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1), //N
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY())); //e
            case EAST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() ), //E
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() - 1), //NE
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1)); //SE
            case SOUTHEAST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() ), //E
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1), //S
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1)); //SE
            case SOUTH:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1 ), //SW
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1), //S
                        CoordinateImpl.makeCoordinate(where.getX() + 1, where.getY() + 1)); //SE
            case SOUTHWEST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1 ), //SW
                        CoordinateImpl.makeCoordinate(where.getX(), where.getY() + 1), //S
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY())); //W
            case WEST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() + 1 ), //SW
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1), //NW
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY())); //W
            case NORTHWEST:
                return Arrays.asList(CoordinateImpl.makeCoordinate(where.getX(), where.getY() - 1 ), //N
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY() - 1), //NW
                        CoordinateImpl.makeCoordinate(where.getX() - 1, where.getY())); //W
            case NONE:
                break;
        }

        throw new IllegalArgumentException("Unable to get zone of control");
    }

    /*
     * @see gettysburg.common.GbgGame#moveUnit(gettysburg.common.GbgUnit,
     * gettysburg.common.Coordinate, gettysburg.common.Coordinate)
     */
    @Override
    public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to) {
        if(movedUnits.contains(unit))
            throw new GbgInvalidMoveException("Already moved unit");
        if (RuleEngine.validateMove(this, unit, from, to)) {
            board.move(unit, from, to);
            movedUnits.add(unit);
        }else
            throw new GbgInvalidMoveException(
                    String.format("Unable to move %s to (%d, %d)", unit.getLeader(), to.getX(), to.getY()));
    }

    public void findPath(GbgUnit unit, Coordinate from, Coordinate to){
        boolean complete = false;
        int steps = 0;

        while(steps < unit.getMovementFactor()){

            steps++;
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

    @Override
    public GbgUnit getUnit(String leader, ArmyID army) {
        return board.findUnit(new GbgUnitImpl(leader, army));
    }

    public Board getBoard() {
        return board;
    }
}
