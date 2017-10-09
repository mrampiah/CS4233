package engine;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import gettysburg.common.exceptions.GbgNotImplementedException;
import model.*;
import util.BattleOrder;
import util.UnitInitializer;
import validators.UnitValidators;
import validators.LocationValidators;
import validators.PlacementValidator;
import validators.StepValidators;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static gettysburg.common.GbgGameStatus.IN_PROGRESS;
import static gettysburg.common.GbgGameStep.*;

public class GameEngine implements GbgGame {
    protected Board board;
    protected GbgGameStep step;
    protected GbgGameStatus status;
    protected int turn;

    public GameEngine() {
        board = new Board();

        getReinforcements();

        //place pieces before turn 1
        turn = 1;
        step = UMOVE; //start on umove
        status = IN_PROGRESS; //and in progress
    }

    @Override
    public GbgGameStep endStep() {
        GameState state = GameState.makeState(board, step);
        switch (step) {
            case UMOVE:
            case CMOVE:
                //special condition for turn 1= remove all stacks
                board.removeAllStacks();
                if (StepValidators.noStacksOnBoard.apply(state))
                    return nextStep();
                break;
            case UBATTLE:
            case CBATTLE:
                if (!UnitValidators.allUnitsParticipated.apply(state))
                    throw new GbgInvalidActionException("All units need to participate");
                return nextStep();
            case GAME_OVER:
                break;
        }
        return null;
    }

    private GbgGameStep nextStep() {
        switch (step) {
            case UMOVE:
                step = UBATTLE;
                board.setAllPossibleBattles(getBattlesToResolve());
                return step;
            case CMOVE:
                board.setAllPossibleBattles(getBattlesToResolve());
                step = CBATTLE;
                return step;
            case UBATTLE:
                //reset board: moved, facing,
                step = CMOVE;
                return step;
            case CBATTLE:
                //reset board: moved, facing,
                step = UMOVE;
                nextTurn();
                return step;
            default:
                return null;
        }
    }

    private void nextTurn() {
        turn++;
        getReinforcements();
    }

    private void getReinforcements() {
        Map<Coordinate, Collection<GbgUnit>> unions = new HashMap<>();
        Map<Coordinate, Collection<GbgUnit>> confeds = new HashMap<>();

        for (UnitInitializer init : BattleOrder.getUnionBattleOrder()
                .stream().filter(init -> init.turn == turn)
                .collect(Collectors.toSet())) {
            Collection<GbgUnit> units = unions.get(init.where);
            if (units == null)
                units = new HashSet<>();

            units.add(Unit.makeUnit(init));
            unions.put(init.where, units);
        }

        for (UnitInitializer init : BattleOrder.getConfederateBattleOrder()
                .stream().filter(init -> init.turn == turn)
                .collect(Collectors.toSet())) {
            Collection<GbgUnit> units = confeds.get(init.where);
            if (units == null)
                units = new HashSet<>();

            units.add(Unit.makeUnit(init));
            confeds.put(init.where, units);
        }

        GameState state = GameState.makeState(board, step);

        for (Coordinate where : unions.keySet()) {
            //only add empty locations
            if (LocationValidators.noUnitsPresent.apply(state, where) && unions.get(where) != null)
                placeUnits(where, unions.get(where));
        }


        for (Coordinate where : confeds.keySet()) {
            //only add empty locations
            if (LocationValidators.noUnitsPresent.apply(state, where) && confeds.get(where) != null)
                placeUnits(where, confeds.get(where));
        }
    }

    @Override
    public int getTurnNumber() {
        return turn;
    }

    /**
     * This method is used to add any reinforcements for the current turn.
     * Should be used only in this special condition, and will allow stacking,
     * unless a unit exists at coordinate
     *
     * @param dest
     * @param units
     */
    public void placeUnits(Coordinate dest, Collection<GbgUnit> units) {
        //test no unit present
        //load placement validators
        List<BiFunction<GameState, Move, Boolean>> moveValidators = new LinkedList<>();
        moveValidators.addAll(Arrays.asList(
                PlacementValidator.noSource));
        //todo: add location validator

        GameState state = GameState.makeState(board, step, moveValidators);

        boolean valid = true;
        for (GbgUnit unit : units) {
            Move move = Move.makePlacement(GameCoordinate.makeCoordinate(dest), unit);

            //if not first turn don't stack
//        if(turn > 1)
//            valid = LocationValidators.noUnitsPresent.apply(state, dest);


            if (!valid) { //only do this if not valid
                for (BiFunction<GameState, Move, Boolean> validator : moveValidators) {
                    if (!validator.apply(state, move))
                        valid = false;
                }
            }
        }
        if (valid)
            board.placeUnits(dest, units);
    }

    @Override
    public Collection<GbgUnit> getUnitsAt(Coordinate where) {
        return board.getUnitsAt(where);
    }

    @Override
    public Collection<BattleDescriptor> getBattlesToResolve() {
        Map<GbgUnit, BattleDescriptor> battles = new HashMap<>();
        GameState state = GameState.makeState(board, step);

        for (GbgUnit attacker : board.getAllAttackingUnits(step)) {
            //skip unit if it's already been in a battle
            if (UnitValidators.unitPreviouslyBattled.apply(GameState.makeState(board, step), attacker))
                continue;

            Collection<GbgUnit> defenders = new HashSet<>();
            //get attacker's zone of control and add all non-empty squares
            Collection<Coordinate> ZOC = GameCoordinate.makeCoordinate(board.getUnitLocation(attacker))
                    .getZoneOfControl(attacker);

            for (Coordinate where : ZOC) {
                if (!LocationValidators.noUnitsPresent.apply(state, where)) {
                    defenders.addAll(board.getUnitsAt(where).stream()
                            .filter(defender -> !UnitValidators.sameArmy.apply(defender, attacker))
                            .collect(Collectors.toSet()));
                }
            }

            if (defenders.size() == 0)
                continue;


            Battle battle = Battle.makeEmptyBattle();
            battle.getAttackers().add(attacker);
            battle.getDefenders().addAll(defenders);

            battles.put(attacker, battle);
        }

        for (GbgUnit defender : board.getAllDefendingUnits(step)) {
            //skip unit if it's already been in a battle
            if (UnitValidators.unitPreviouslyBattled.apply(GameState.makeState(board, step), defender))
                continue;

            //get defender's zone of control
            Collection<Coordinate> ZOC = GameCoordinate.makeCoordinate(board.getUnitLocation(defender))
                    .getZoneOfControl(defender);
            Collection<GbgUnit> attackers = new HashSet<>();

            for (Coordinate where : ZOC) {
                //someone in zoc (attacker)
                if (!LocationValidators.noUnitsPresent.apply(state, where)) {
                    attackers.addAll(board.getUnitsAt(where).stream()
                            .filter(attacker -> !UnitValidators.sameArmy.apply(attacker, defender))
                            .collect(Collectors.toSet()));
                }
            }

            //don't add if no attackers
            if (attackers.size() == 0)
                continue;

            Battle battle = Battle.makeEmptyBattle();
            battle.getDefenders().add(defender);
            battle.getAttackers().addAll(attackers);

            battles.put(defender, battle);
        }
        return battles.values();
    }

    @Override
    public Coordinate whereIsUnit(GbgUnit unit) {
        return board.getUnitLocation(unit);
    }

    @Override
    public GbgGameStep getCurrentStep() {
        return step;
    }

    @Override
    public void moveUnit(GbgUnit unit, Coordinate from, Coordinate to) {
        Move move = Move.makeMove(GameCoordinate.makeCoordinate(from), GameCoordinate.makeCoordinate(to), unit);
        GameState state = GameState.makeState(board, step);
        //first check if unit matches player
        if (!UnitValidators.matchingPlayer.apply(unit, step))
            throw new GbgInvalidMoveException("Not the right player(side)");

        if (!LocationValidators.unitAtSrc.apply(state, move))
            throw new GbgInvalidMoveException("Unit not present at source");

        if (!UnitValidators.moveWithinMovementFactor.apply(move))
            throw new GbgInvalidMoveException("Can't move beyond movement factor");

        if (UnitValidators.unitMoved.apply(state, unit))
            throw new GbgInvalidMoveException("Unit already moved this turn");

        if (!LocationValidators.noUnitsPresent.apply(state, to))
            throw new GbgInvalidMoveException("Can't move to occupied location");

        //not through any zones of control
        if (!LocationValidators.notThroughZoneOfControl.apply(state, move))
            throw new GbgInvalidMoveException("Can't move through zone of control");

        //if everything passes:
        board.moveUnit(from, to, unit);
    }

    @Override
    public Direction getUnitFacing(GbgUnit unit) {
        return board.getUnitFacing(unit);
    }

    @Override
    public void setUnitFacing(GbgUnit unit, Direction direction) {
        GameState state = GameState.makeState(board, step);

        //correct move
        if (!UnitValidators.matchingPlayer.apply(unit, step))
            throw new GbgInvalidMoveException("Can't change facing while other player's turn");

        if (UnitValidators.facingChanged.apply(state, unit))
            throw new GbgInvalidMoveException("Facing has already been changed");

        board.setUnitFacing(unit, direction);
    }

    @Override
    public BattleResolution resolveBattle(BattleDescriptor battle) {
        GameState state = GameState.makeState(board, step);
        //ensure all attackers from attacking side
        //validate attacking units
        battle.getAttackers().forEach(unit -> {
            if (!UnitValidators.correctArmyAttacking.apply(state, unit))
                throw new GbgInvalidActionException("Attacking unit not of right color");
        });

        //validate defending units
        battle.getDefenders().forEach(unit -> {
            if (!UnitValidators.correctArmyDefending.apply(state, unit))
                throw new GbgInvalidActionException("Defending unit not of right color");
        });

        //can't have empty attack or defense or both
        if (UnitValidators.emptyAttackingUnit.apply(battle) || UnitValidators.emptyDefendingUnit.apply(battle))
            throw new GbgInvalidActionException("Can't have battle with empty side(s)");

        //ensure all units are first participants
        for (GbgUnit unit : battle.getDefenders()) {
            if (UnitValidators.unitPreviouslyBattled.apply(state, unit))
                throw new GbgInvalidActionException("Can't battle twice");
        }

        for (GbgUnit unit : battle.getAttackers()) {
            if (UnitValidators.unitPreviouslyBattled.apply(state, unit))
                throw new GbgInvalidActionException("Can't battle twice");
        }

        if (!LocationValidators.validateZonesOfControl.apply(state, battle))
            throw new GbgInvalidActionException("Unit's not in battle position");

        int attack = 0;
        for (GbgUnit attacker : battle.getAttackers()) {
            attack += attacker.getCombatFactor();
        }

        int defense = 0;
        for (GbgUnit defender : battle.getDefenders()) {
            defense += defender.getCombatFactor();
        }

        Resolution resolution = Resolution.makeEmptyResolution();

        resolution.setBattleResult(getBattleResult(attack, defense));

        //resolve battle
        switch (resolution.getBattleResult()) {
            case AELIM:
                eliminateAttackers(battle, resolution);
                break;
            case DELIM:
                eliminateDefenders(battle, resolution);
                break;
            case EXCHANGE:
                if (attack > defense)//remove all defending pieces
                    eliminateDefenders(battle, resolution);
                else if (defense > attack) //remove all attacking pieces
                    eliminateAttackers(battle, resolution);
                break;
            default:
                throw new GbgNotImplementedException("Unimplemented battle results");
        }

        board.applyResolution(resolution);
        return resolution;
    }

    private void eliminateAttackers(BattleDescriptor battle, BattleResolution resolution) {
        Collection<GbgUnit> active = new HashSet<>(battle.getAttackers());
        if (step == UBATTLE) {
            resolution.getEliminatedUnionUnits().addAll(battle.getAttackers());

            //remove and add the rest to active units
            active.removeAll(resolution.getEliminatedUnionUnits());
            resolution.getActiveUnionUnits().addAll(active);

            //defenders should all be active
            resolution.getActiveConfederateUnits().addAll(battle.getDefenders());
        } else if (step == CBATTLE) {
            resolution.getEliminatedConfederateUnits().addAll(battle.getAttackers());

            //remove and add the rest to active units
            active.removeAll(resolution.getEliminatedConfederateUnits());
            resolution.getActiveConfederateUnits().addAll(active);

            //defenders should all be active
            resolution.getActiveUnionUnits().addAll(battle.getDefenders());
        }
    }

    private void eliminateDefenders(BattleDescriptor battle, BattleResolution resolution) {
        Collection<GbgUnit> active = new HashSet<>(battle.getDefenders());
        if (step == UBATTLE) {
            resolution.getEliminatedConfederateUnits().addAll(battle.getDefenders());

            //remove and add the rest to active units
            active.removeAll(resolution.getEliminatedConfederateUnits());
            resolution.getActiveConfederateUnits().addAll(active);

            //attackers should all be active
            resolution.getActiveUnionUnits().addAll(battle.getAttackers());
        } else if (step == CBATTLE) {
            resolution.getEliminatedUnionUnits().addAll(battle.getDefenders());
            //remove and add the rest to active units
            active.removeAll(resolution.getEliminatedUnionUnits());
            resolution.getActiveUnionUnits().addAll(active);

            //attackers should all be active
            resolution.getActiveConfederateUnits().addAll(battle.getAttackers());
        }
    }

    private BattleResult getBattleResult(int attack, int defense) {
        double odds = (double) attack / (double) defense;
        if (odds >= 2.0)
            return BattleResult.DELIM;
        else if (0.5 < odds && odds < 2.0)
            return BattleResult.EXCHANGE;
        else if (odds <= 0.5)
            return BattleResult.AELIM;

        throw new GbgInvalidActionException("Couldn't resolve battle");
    }


    @Override
    public Coordinate whereIsUnit(String leader, ArmyID army) {
        return board.getUnitLocation(Unit.makeBasicUnit(army, leader));
    }

    @Override
    public GbgUnit getUnit(String leader, ArmyID army) {
        return board.getUnit(Unit.makeBasicUnit(army, leader));
    }

    @Override
    public GbgGameStatus getGameStatus() {
        return status;
    }

    public Board getBoard() {
        return board;
    }
}
