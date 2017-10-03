package student.gettysburg.engine.common;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import org.junit.Rule;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RuleEngine {
    private static GbgGame game;

    public static Predicate<Coordinate> destinationOnBoardValidator =
            (coord -> coord.getX() <= GbgBoard.COLUMNS && coord.getY() <= GbgBoard.ROWS);
    public static Predicate<Coordinate> noUnitsPresent =
            (coord -> game.getUnitsAt(coord) == null || game.getUnitsAt(coord).size() == 0);

    private static Predicate<Coordinate> unitArmyMatchesCurrentStep =
            (coord -> {
                if (noUnitsPresent.test(coord))
                    return false; //todo: should probably throw error instead

                //should be only one unit at coord so this should be fine
                GbgUnit unit = game.getUnitsAt(coord).iterator().next();

                boolean confed = unit.getArmy() == ArmyID.CONFEDERATE;
                boolean union = unit.getArmy() == ArmyID.UNION;
                boolean umove = game.getCurrentStep() == GbgGameStep.UMOVE;
                boolean ubattle = game.getCurrentStep() == GbgGameStep.UBATTLE;
                boolean cmove = game.getCurrentStep() == GbgGameStep.CMOVE;
                boolean cbattle = game.getCurrentStep() == GbgGameStep.CBATTLE;
                return ((union && umove)  || confed && cmove) || ((union && ubattle) ||confed && cbattle);
            });

    private static BiPredicate<Coordinate, Coordinate> differingSourceDestValidator = ((c1, c2) -> !c1.equals(c2));
    private static BiPredicate<Coordinate, Coordinate> withinMovementFactorValidator =
            ((src, dest) -> {
                if (noUnitsPresent.test(src))
                    return false; //todo: should probably throw error

                GbgUnit unit = game.getUnitsAt(src).iterator().next();

                BFS.evaluate(src, game);
                //account for current square which is included in result
                List<Coordinate> result = BFS.shortestPath(dest);
                if (result == null)
                    throw new GbgInvalidMoveException("no valid paths");
                return unit.getMovementFactor() >= result.size() - 1;
            });
    public static BiPredicate<Coordinate, Coordinate> adjacent =
            ((coord1, coord2) -> coord1.distanceTo(coord2) == 1);


    private RuleEngine(GbgGame game) {
            RuleEngine.game = game;
    }

    public static RuleEngine makeRuleEngine(GbgGame game) {
        return new RuleEngine(game);
    }

    public static RuleEngine getRuleEngine() {
        if (RuleEngine.game != null)
            return new RuleEngine(RuleEngine.game);

        return null;
    }


    public Predicate<Coordinate> notControlZone = (coord -> {
        //set squares with units present
        List<Coordinate> nonEmpty = Board.getAdjacentSquares(coord).stream()
                .filter(coordinate -> noUnitsPresent.test(coordinate)).collect(Collectors.toList());

        for (Coordinate coordinate : nonEmpty) {
            try {
                for (GbgUnit unit : game.getUnitsAt(coordinate)) {
                    if (((GettysburgEngine) game).getZoneOfControl(unit).contains(coord))
                        return false;
                }
            } catch (NullPointerException ex) {
            }
        }

        return true;
    });


    public boolean validateMove(Coordinate from, Coordinate to) {
        boolean dest = destinationOnBoardValidator.test(to);
        boolean different = differingSourceDestValidator.test(from, to);
        boolean withinBounds = withinMovementFactorValidator.test(from, to);
        boolean unitsAtSrc = !noUnitsPresent.test(from);
        boolean noUnitsAtDest = noUnitsPresent.test(to);
        boolean matchingUnit = unitArmyMatchesCurrentStep.test(from);

        return dest && different && withinBounds && noUnitsAtDest && unitsAtSrc && matchingUnit;
    }

    public boolean validatePlacement(Coordinate where, GbgUnit unit, boolean replace) {
        boolean empty = replace ? true : noUnitsPresent.test(where);

        return empty;
    }
}
