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
import java.util.stream.Collectors;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import gettysburg.common.exceptions.GbgNotImplementedException;
import student.gettysburg.engine.exceptions.UnitNotFoundException;
import student.gettysburg.engine.utility.configure.BattleOrder;
import student.gettysburg.engine.utility.configure.UnitInitializer;

import static gettysburg.common.ArmyID.CONFEDERATE;
import static gettysburg.common.ArmyID.UNION;

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
    protected Board board;
    protected int turn = 0;
    protected GbgGameStep step;
    protected Set<GbgUnit> movedUnits;
    protected boolean callToResolveBattles = false;


    public GettysburgEngine() {
        setRuleEngine(this);
        //initialized moved units collection
        movedUnits = new HashSet<>();
        board = new Board();
        initGame();

        //change turn and start union move todo: move to endstep
        turn = 1;
        step = GbgGameStep.UMOVE; // start on union move
    }

    public void setRuleEngine(GettysburgEngine game){
        RuleEngine.makeRuleEngine(game);
    }

    private void initGame() {
        //turn 0 actions
        placeAllEntryPieces();

//        while(turn <= 49){
//            List<GbgUnit> unionReinforcements = BattleOrder.getUnionBattleOrder().stream()
//                    .filter(unit -> unit.turn == turn)
//                    .map(unit -> unit.unit).collect(Collectors.toList());
//
//
//
//            turn++;
//        }
    }

    private void placeAllEntryPieces() {
        //get union pieces
        List<UnitInitializer> unionPieces = BattleOrder.getUnionBattleOrder().stream()
                .filter(init -> init.turn == turn)
                .collect(Collectors.toList());

        board.addUnits(unionPieces, true);

        //get confederate pieces
        List<UnitInitializer> confedPieces = BattleOrder.getConfederateBattleOrder().stream()
                .filter(init -> init.turn == turn)
                .collect(Collectors.toList());

        board.addUnits(confedPieces, true);
    }

    /*
     * @see gettysburg.common.GbgGame#endBattleStep()
     */
    @Deprecated
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

        //reset for next battle round
        callToResolveBattles = false;
    }

    /*
     * @see gettysburg.common.GbgGame#endMoveStep()
     */
    @Deprecated
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

        //ensure no stacks on board
        board.removeStacks();
    }

    /*
     * @see gettysburg.common.GbgGame#endStep()
     */
    @Override
    public GbgGameStep endStep() {
        switch (step) {
            case CBATTLE:
            case UBATTLE:
                if (!callToResolveBattles)
                    throw new GbgInvalidActionException("getBattlesToResolve not called");
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
        callToResolveBattles = true;
        switch (step) {
            case UBATTLE:
                Collection<BattleDescriptor> descriptors = new HashSet<>();
                //get only units that haven't battled yet
                Collection<GbgUnit> attackers = getAllUnits(UNION).stream()
                        .filter(unit -> !((GbgUnitImpl) unit).isBattled())
                        .collect(Collectors.toList());

                Collection<Coordinate> defense = new HashSet<>();

                //get any units present in attacker's zone of control
                Collection<GbgUnit> defenders = new HashSet<>();
                for (GbgUnit unit : attackers) {
                    for(Coordinate c : getZoneOfControl(unit)){
                        if(!RuleEngine.noUnitsPresent.test(c))
                            defense.add(c);
                    }

                    //get defending units
                    for (Coordinate c : defense) {
                        GbgUnit next = getUnitsAt(c).iterator().next();
                        if (next != null && next.getArmy() != UNION)
                            defenders.add(getUnitsAt(c).iterator().next());
                    }

                    //add to battle descriptors only if valid battle
                    if (defenders.size() > 0 && attackers.size() > 0)
                        descriptors.add(new BattleDescriptorImpl(attackers, defenders));
                }
                return descriptors;
            case CBATTLE:
                break;
        }

        // TODO Auto-generated method stub
        return null;
    }

    public List<GbgUnit> getAllUnits(ArmyID id) {
        return board.getAllUnits(id);
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
        if (whoWon() != null)
            return whoWon();
        else
            return GbgGameStatus.IN_PROGRESS;
    }

    private GbgGameStatus whoWon() {
        return null;
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

        if (result == null)
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
     * relay to board to compute unit's zone of control
     */
    public Collection<Coordinate> getZoneOfControl(GbgUnit unit) {
        return board.getZoneOfControl(unit);
    }

    /*
     * @see gettysburg.common.GbgGame#moveUnit(gettysburg.common.GbgUnit,
     * gettysburg.common.Coordinate, gettysburg.common.Coordinate)
     */
    @Override
    public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to) {
        if (movedUnits.contains(unit))
            throw new GbgInvalidMoveException("Already moved unit");
        if (RuleEngine.getRuleEngine().validateMove(from, to)) {
            board.move(unit, from, to);
            movedUnits.add(unit);
        } else
            throw new GbgInvalidMoveException(
                    String.format("Unable to move %s to (%d, %d)", unit.getLeader(), to.getX(), to.getY()));
    }

    /*
     * @see gettysburg.common.GbgGame#resolveBattle(int)
     */
    @Override
    public BattleResolution resolveBattle(BattleDescriptor battle) {
        ArmyID attackingArmy = battle.getAttackers().iterator().next().getArmy();
        ArmyID defendingArmy = battle.getDefenders().iterator().next().getArmy();

        int attack = 0;
        for (GbgUnit attacker : battle.getAttackers()) {
            attack += attacker.getCombatFactor();
        }

        int defense = 0;
        for (GbgUnit defender : battle.getDefenders()) {
            defense += defender.getCombatFactor();
        }

        BattleResult result = battleResult(attack, defense);
        Collection<GbgUnit> eliminatedUnionUnits = new HashSet<>();
        Collection<GbgUnit> eliminatedConfederateUnits = new HashSet<>();
        Collection<GbgUnit> activeUnionUnits = new HashSet<>();
        Collection<GbgUnit> activeConfederateUnits = new HashSet<>();

        //resolve battle
        switch (result) {
            case AELIM:
                if (attackingArmy == UNION)
                    eliminatedUnionUnits.addAll(battle.getAttackers());
                else if (attackingArmy == CONFEDERATE)
                    eliminatedConfederateUnits.addAll(battle.getAttackers());
                break;
            case DELIM:
                if (defendingArmy == UNION)
                    eliminatedUnionUnits.addAll(battle.getAttackers());
                else if (defendingArmy == CONFEDERATE)
                    eliminatedConfederateUnits.addAll(battle.getAttackers());
                break;

            case EXCHANGE:
                if (attack > defense) {
                    //remove all defending pieces
                    if (defendingArmy == UNION)
                        eliminatedUnionUnits.addAll(battle.getDefenders());
                    else if (defendingArmy == CONFEDERATE)
                        eliminatedConfederateUnits.addAll(battle.getDefenders());
                }else if(defense > attack){
                    //remove all attacking pieces
                    if (attackingArmy == UNION)
                        eliminatedUnionUnits.addAll(battle.getAttackers());
                    else if (attackingArmy == CONFEDERATE)
                        eliminatedConfederateUnits.addAll(battle.getAttackers());
                }
                break;
            case ABACK:
            case DBACK:
            case AELIMNR:
            case DELIMNR:
                throw new GbgNotImplementedException("Unimplemented battle results");
        }

        //get active units
        if(attackingArmy == UNION){
            activeUnionUnits = new HashSet<>(battle.getAttackers());
            activeUnionUnits.removeAll(eliminatedUnionUnits);

            activeConfederateUnits = new HashSet<>(battle.getDefenders());
            activeConfederateUnits.removeAll(eliminatedConfederateUnits);
        }else if(attackingArmy == CONFEDERATE){
            activeConfederateUnits = new HashSet<>(battle.getAttackers());
            activeConfederateUnits.removeAll(eliminatedConfederateUnits);

            activeUnionUnits = new HashSet<>(battle.getDefenders());
            activeUnionUnits.removeAll(eliminatedUnionUnits);
        }

        BattleResolution resolution = new Resolution(result, eliminatedUnionUnits, eliminatedConfederateUnits,
                activeUnionUnits, activeConfederateUnits);
        return resolution;
    }

    private BattleResult battleResult(int attack, int defense) {
        double odds = attack / defense;
        if (odds >= 2.0)
            return BattleResult.DELIM;
        else if (0.5 < odds && odds < 2.0)
            return BattleResult.EXCHANGE;
        else if (odds <= 0.5)
            return BattleResult.AELIM;

        throw new GbgInvalidActionException("Couldn't resolve battle");
    }

    /*
     * @see gettysburg.common.GbgGame#setUnitFacing(gettysburg.common.GbgUnit,
     * gettysburg.common.Direction)
     */
    @Override
    public void setUnitFacing(GbgUnit unit, Direction direction) {
        if (((GbgUnitImpl) unit).isFacingChanged())
            throw new GbgInvalidMoveException("Can't change facing twice");

        if (step == GbgGameStep.CBATTLE || step == GbgGameStep.UBATTLE)
            throw new GbgInvalidMoveException("Wrong time to change facing");

        if ((unit.getArmy() == UNION && step == GbgGameStep.CMOVE) ||
                unit.getArmy() == ArmyID.CONFEDERATE && step == GbgGameStep.UMOVE)
            throw new GbgInvalidMoveException("Not your turn");

        unit.setFacing(direction);
        board.setUnit(whereIsUnit(unit), unit);
    }

    public void setCurrentStep(GbgGameStep step){
        this.step = step;
    }

    public void setTurn(int turn){
        this.turn = turn;
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
