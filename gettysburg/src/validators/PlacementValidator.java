package validators;

import model.GameState;
import model.Move;

import java.util.function.BiFunction;
public class PlacementValidator {
    public static BiFunction<GameState, Move, Boolean> noSource =
            ((state, move ) ->  move.src == null);
}
