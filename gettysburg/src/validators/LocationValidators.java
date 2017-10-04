package validators;

import gettysburg.common.Coordinate;
import gettysburg.common.GbgBoard;
import model.GameState;
import model.Move;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LocationValidators {

    public static Function<Coordinate, Boolean> locationOnBoardValidator =
            ( where -> where != null &&  ((1 <= where.getX() && where.getX() <= GbgBoard.COLUMNS) &&
                    (1 <= where.getY() && where.getY() <= GbgBoard.ROWS)));
}
