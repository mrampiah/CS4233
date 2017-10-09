package validators;

import gettysburg.common.BattleDescriptor;
import gettysburg.common.Coordinate;
import gettysburg.common.GbgBoard;
import gettysburg.common.GbgUnit;
import model.GameCoordinate;
import model.GameState;
import model.Move;
import util.BFS;

import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LocationValidators {

    public static Function<Coordinate, Boolean> locationOnBoardValidator =
            ( where -> where != null &&  ((1 <= where.getX() && where.getX() <= GbgBoard.COLUMNS) &&
                    (1 <= where.getY() && where.getY() <= GbgBoard.ROWS)));

    public static BiFunction<GameState, Coordinate, Boolean> noUnitsPresent =
            ((state, where) -> state.board.getUnitsAt(where) == null ||
                    state.board.getUnitsAt(where).size() == 0);

    public static BiFunction<GameState, Move, Boolean> unitAtSrc =
            ((state, move) -> state.board.getUnitLocation(move.unit).equals(move.src));

    public static BiFunction<GameState, Coordinate, Boolean> isZoneOfControl =
            ((state, where) -> state.board.getActiveZonesOfControl(state.step).contains(where));

    public static BiFunction<GameState, Move, Boolean> notThroughZoneOfControl = ((state, move) -> {
        //initiate bfs for possible paths
        BFS.evaluate(move.src);
        List<Coordinate> path = BFS.shortestPath(move.dest);
        Collection<Coordinate> activeZones = state.board.getActiveZonesOfControl(state.step);
        //return true if no active zones or destination in active zone
        if((activeZones == null || activeZones.size() == 0))
            return true;

        for(Coordinate where : path){
            if(activeZones.contains(where) && !where.equals(move.dest))
                return false;
        }

        //if no possible paths exit the inner loop, return false
        return true;
    });

    public static BiFunction<GameState, BattleDescriptor, Boolean> validateZonesOfControl = ((state, battle) -> {
        boolean valid = true;
        for(GbgUnit attacker : battle.getAttackers()){
            for(GbgUnit defender : battle.getDefenders()){
                GameCoordinate aCoord = GameCoordinate.makeCoordinate(state.board.getUnitLocation(attacker));
                GameCoordinate dCoord = GameCoordinate.makeCoordinate(state.board.getUnitLocation(defender));
                //if neither is in the other's zoc, they can't be in battle
                valid = aCoord.getZoneOfControl(attacker).contains(dCoord) ||
                        dCoord.getZoneOfControl(defender).contains(aCoord);
                if(valid == false)
                    break;
            }
        }
        return valid;
    });
}
