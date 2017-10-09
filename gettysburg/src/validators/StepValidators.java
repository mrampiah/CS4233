package validators;

import gettysburg.common.GbgUnit;
import model.GameState;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class StepValidators {
    //all battles resolved
    public static Function<GameState, Boolean> allBattlesResolved = (state -> {
        //get all battles to resolve
        return false;
    });

    //no stacks on board
    public static Function<GameState, Boolean> noStacksOnBoard = (state -> {
       //loop through map and if any with more than 1 unit, return false, else true
       for(Collection<GbgUnit> units : state.board.units.values()){
            if(units != null && units.size() > 1)
                return false;
        }
        return true;
    });

    //all units participated
}
