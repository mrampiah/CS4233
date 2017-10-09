package validators;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gettysburg.common.*;
import model.GameState;
import model.Move;

import java.util.function.BiFunction;
import java.util.function.Function;

import static gettysburg.common.ArmyID.CONFEDERATE;
import static gettysburg.common.ArmyID.UNION;
import static gettysburg.common.GbgGameStep.CBATTLE;
import static gettysburg.common.GbgGameStep.UBATTLE;

public class UnitValidators {
    public static BiFunction<GameState, GbgUnit, Boolean> unitPreviouslyBattled =
    ((state, unit) -> state.board.battled.contains(unit));

    public static BiFunction<GbgUnit, GbgUnit, Boolean> sameArmy = ((a, b) -> a.getArmy().equals(b.getArmy()));

    public static BiFunction<GbgUnit, GbgGameStep, Boolean> matchingPlayer = ((unit, step) ->{
       if(step == GbgGameStep.UMOVE || step == UBATTLE)
           return unit.getArmy() == UNION;

       return unit.getArmy() == CONFEDERATE;
    });

    public static Function<Move, Boolean> moveWithinMovementFactor =
            (move -> {
                try {
                    return move.src.distanceTo(move.dest) <= move.unit.getMovementFactor();
                }catch(NullPointerException e){
                    return false;
                }
            });

    public static BiFunction<GameState, GbgUnit, Boolean> facingChanged = ((state, unit) ->
            state.board.faceChanged.contains(unit));

    public static BiFunction<GameState, GbgUnit, Boolean> unitMoved = ((state, unit) ->
            state.board.moved.contains(unit));

    public static Function<GameState, Boolean> allUnitsParticipated =
            (state -> state.board.battled.size() == state.board.possibleBattles.size() ||
                    state.board.battled.containsAll(state.board.possibleBattles));

    public static BiFunction<GameState, GbgUnit, Boolean> correctArmyAttacking = ((state, unit) ->
         (state.step == UBATTLE && unit.getArmy() == UNION) || (state.step == CBATTLE && unit.getArmy() == CONFEDERATE));


    public static BiFunction<GameState, GbgUnit, Boolean> correctArmyDefending = ((state, unit) ->
            (state.step == UBATTLE && unit.getArmy() == CONFEDERATE) || (state.step == CBATTLE && unit.getArmy() == UNION));

    public static Function<BattleDescriptor, Boolean> emptyAttackingUnit =
            (battle -> battle.getAttackers() == null || battle.getAttackers().size() == 0);

    public static Function<BattleDescriptor, Boolean> emptyDefendingUnit =
            (battle -> battle.getDefenders() == null || battle.getDefenders().size() == 0);

}
