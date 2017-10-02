package student.gettysburg.engine.common;

import gettysburg.common.*;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RuleEngine {

    private static Predicate<Coordinate> destinationOnBoardValidator =
            (coord -> coord.getX() <= GbgBoard.COLUMNS && coord.getY() <= GbgBoard.ROWS);
    private static BiPredicate<Coordinate, GbgGame> noUnitsPresent =
            ((coord, game) -> game.getUnitsAt(coord) == null || game.getUnitsAt(coord).size() == 0);
    private static BiPredicate<GbgUnit, GbgGameStep> unitArmyMatchesCurrentStep =
            ((unit, step) -> (unit.getArmy() == ArmyID.UNION && step == GbgGameStep.UMOVE)
                    || (unit.getArmy() == ArmyID.CONFEDERATE && step == GbgGameStep.CMOVE));
    private static BiPredicate<Coordinate, Coordinate> differingSourceDestValidator = ((c1, c2) -> !c1.equals(c2));
    private static BiPredicate<GbgUnit, CoordinatePair> withinMovementFactorValidator =
            ((unit, pair) -> unit.getMovementFactor() >= pair.getCoordinate1().distanceTo(pair.getCoordinate2()));
    public static Predicate<CoordinatePair> adjacent =
            (pair -> pair.getCoordinate1().distanceTo(pair.getCoordinate2()) == 1);


    public static boolean validateMove(GbgGame game, GbgUnit unit, Coordinate from, Coordinate to) {
        boolean dest = destinationOnBoardValidator.test(to);
        boolean different = differingSourceDestValidator.test(from, to);
        boolean withinBounds = withinMovementFactorValidator.test(unit, new CoordinatePair(from, to));
        boolean unitsAtSrc = !noUnitsPresent.test(from, game);
        boolean noUnitsAtDest = noUnitsPresent.test(to, game);
        boolean matchingUnit = unitArmyMatchesCurrentStep.test(unit, game.getCurrentStep());


        return dest && different && withinBounds && noUnitsAtDest && unitsAtSrc && matchingUnit;
    }
}
