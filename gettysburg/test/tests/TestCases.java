package tests;

import static gettysburg.common.ArmyID.CONFEDERATE;
import static gettysburg.common.ArmyID.UNION;
import static gettysburg.common.BattleResult.AELIM;
import static gettysburg.common.Direction.*;
import static gettysburg.common.GbgGameStep.UBATTLE;
import static gettysburg.common.GbgGameStep.UMOVE;
import static model.GameCoordinate.makeCoordinate;
import static org.junit.Assert.*;

import engine.GameEngine;
import engine.TestGameEngine;
import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import model.Battle;
import model.GameCoordinate;
import model.GameState;
import model.Unit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import engine.Factory;
import util.BFS;
import util.BattleOrder;
import util.PathFinder;
import validators.LocationValidators;
import validators.StepValidators;

import java.nio.file.Path;
import java.util.*;

public class TestCases {
    GameEngine game;
    TestGameEngine testGame;
    GbgUnit heth, hill, gamble, devin, reynolds, wadsworth, robinson, rowley;

    @Before
    public void setup() {
        game = (GameEngine) Factory.makeGame();
        testGame = Factory.makeTestGame();

        //initialize

        //union
        gamble = game.getUnit("Gamble", UNION);
        devin = game.getUnit("Devin", UNION);
        reynolds = game.getUnit("Reynolds", UNION);
        wadsworth = game.getUnit("Wadsworth", UNION);
        robinson = game.getUnit("Robinson", UNION);
        rowley = game.getUnit("Rowley", UNION);

        //confed
        heth = game.getUnit("Heth", CONFEDERATE);
        hill = BattleOrder.getConfederateBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Hill")).findFirst().get().unit;

    }

    @Test
    public void testMakeEngine() {
        assertNotNull(game);
    }

    @Test
    public void testMakeTestGameEngine() {
        assertNotNull(testGame);
    }

    @Test
    public void testGameStartAtTurn1() {
        assertEquals(1, game.getTurnNumber(), 0);
    }

    @Test
    public void testAllUnitsPresentAtStart() {
        //confed
        assertTrue(game.getUnitsAt(makeCoordinate(8, 8)).contains(heth));

        //union
        assertTrue(game.getUnitsAt(makeCoordinate(11, 11)).contains(gamble));
        assertTrue(game.getUnitsAt(makeCoordinate(13, 9)).contains(devin));
        assertTrue(game.getUnitsAt(makeCoordinate(7, 28)).contains(reynolds));
        assertTrue(game.getUnitsAt(makeCoordinate(7, 28)).contains(wadsworth));
        assertTrue(game.getUnitsAt(makeCoordinate(7, 28)).contains(robinson));
        assertTrue(game.getUnitsAt(makeCoordinate(7, 28)).contains(rowley));
    }


    @Test
    public void testGetNeighborsAtCorner() {
        GameCoordinate where = makeCoordinate(1, 1);
        Collection<Coordinate> neighbors = where.getNeighbors();

        assertEquals(3, neighbors.size(), 0);
        assertTrue(neighbors.contains(makeCoordinate(1, 2)));
        assertTrue(neighbors.contains(makeCoordinate(2, 1)));
        assertTrue(neighbors.contains(makeCoordinate(2, 2)));
    }

    @Test(expected = GbgInvalidCoordinateException.class)
    public void testLocationValidator(){
        makeCoordinate(0, 1);
    }

    @Test
    public void testLocationValidatorBottomRow(){
        makeCoordinate(7, 28);
    }

    @Test
    public void testGetNeighborsAtCenter() {
        GameCoordinate where = makeCoordinate(5, 5);
        Collection<Coordinate> neighbors = where.getNeighbors();

        assertEquals(8, neighbors.size(), 0);
        assertTrue(neighbors.contains(makeCoordinate(4, 5)));
        assertTrue(neighbors.contains(makeCoordinate(4, 4)));
        assertTrue(neighbors.contains(makeCoordinate(5, 4)));
        assertTrue(neighbors.contains(makeCoordinate(6, 4)));
        assertTrue(neighbors.contains(makeCoordinate(6, 5)));
        assertTrue(neighbors.contains(makeCoordinate(6, 6)));
        assertTrue(neighbors.contains(makeCoordinate(5, 6)));
        assertTrue(neighbors.contains(makeCoordinate(4, 6)));
    }


    @Test
    public void testDistanceToSouth() {
        Coordinate src = makeCoordinate(1, 1);
        Coordinate dest = makeCoordinate(1, 4);

        assertEquals(3, src.distanceTo(dest), 0);
    }

    @Test
    public void testDistanceToSouthEast() {
        Coordinate src = makeCoordinate(5, 8);
        Coordinate dest = makeCoordinate(10, 20);

        assertEquals(12, src.distanceTo(dest), 0);
    }
    //todo: add other directions

    @Test
    public void testPlaceUnit(){
        Coordinate src = makeCoordinate(5, 8);
        Collection<GbgUnit> units = new HashSet<>();
        units.add(heth);
        testGame.placeUnits(src, units);
    }

    @Test
    public void testDirectionAndZOCToNorth(){
        assertEquals(NORTH, makeCoordinate(20, 7).directionTo(makeCoordinate(20, 1)));

        //using heth
        heth.setFacing(NORTH);
        Collection<Coordinate> zoneOfControl = makeCoordinate(20, 7).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(19, 6)));
        assertTrue(zoneOfControl.contains(makeCoordinate(20, 6)));
        assertTrue(zoneOfControl.contains(makeCoordinate(21, 6)));
    }

    @Test
    public void testDirectionAndZOCToSouth(){
        assertEquals(SOUTH, makeCoordinate(15, 7).directionTo(makeCoordinate(15, 18)));

        //using heth
        heth.setFacing(SOUTH);
        Collection<Coordinate> zoneOfControl = makeCoordinate(15,18).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(14, 19)));
        assertTrue(zoneOfControl.contains(makeCoordinate(15, 19)));
        assertTrue(zoneOfControl.contains(makeCoordinate(16, 19)));
    }

    @Test
    public void testDirectionAndZOCToEast(){
        assertEquals(EAST, makeCoordinate(10, 4).directionTo(makeCoordinate(15, 4)));

        //using heth
        heth.setFacing(EAST);
        Collection<Coordinate> zoneOfControl = makeCoordinate(10, 4).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(11, 3)));
        assertTrue(zoneOfControl.contains(makeCoordinate(11, 4)));
        assertTrue(zoneOfControl.contains(makeCoordinate(11, 5)));
    }

    @Test
    public void testDirectionAndZOCToWest(){
        assertEquals(WEST, makeCoordinate(22, 10).directionTo(makeCoordinate(12, 10)));

        //using heth
        heth.setFacing(WEST);
        Collection<Coordinate> zoneOfControl = makeCoordinate(12, 10).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(11, 9)));
        assertTrue(zoneOfControl.contains(makeCoordinate(11, 10)));
        assertTrue(zoneOfControl.contains(makeCoordinate(11, 11)));
    }

    @Test
    public void testDirectionAndZOCToNorthWest(){
        assertEquals(NORTHWEST, makeCoordinate(11, 7).directionTo(makeCoordinate(9, 4)));

        //using heth
        heth.setFacing(NORTHWEST);
        Collection<Coordinate> zoneOfControl = makeCoordinate(11, 7).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(10, 7)));
        assertTrue(zoneOfControl.contains(makeCoordinate(10, 6)));
        assertTrue(zoneOfControl.contains(makeCoordinate(11, 6)));
    }

    @Test
    public void testDirectionAndZOCToNorthEast(){
        assertEquals(NORTHEAST, makeCoordinate(19, 15).directionTo(makeCoordinate(20, 1)));

        //using heth
        heth.setFacing(NORTHEAST);
        Collection<Coordinate> zoneOfControl = makeCoordinate(20, 2).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(21, 2)));
        assertTrue(zoneOfControl.contains(makeCoordinate(21, 1)));
        assertTrue(zoneOfControl.contains(makeCoordinate(20, 1)));
    }

    @Test
    public void testDirectionAndZOCToSouthEast(){
        assertEquals(SOUTHEAST, makeCoordinate(20, 7).directionTo(makeCoordinate(22, 28)));

        //using heth
        heth.setFacing(SOUTHEAST);
        Collection<Coordinate> zoneOfControl = makeCoordinate(20, 7).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(20, 8)));
        assertTrue(zoneOfControl.contains(makeCoordinate(21, 7)));
        assertTrue(zoneOfControl.contains(makeCoordinate(21, 8)));
    }

    @Test
    public void testDirectionAndZOCToSouthWest(){
        assertEquals(SOUTHWEST, makeCoordinate(10, 7).directionTo(makeCoordinate(2, 11)));

        //using heth
        heth.setFacing(SOUTHWEST);
        Collection<Coordinate> zoneOfControl = makeCoordinate(2, 11).getZoneOfControl(heth);

        assertTrue(zoneOfControl.contains(makeCoordinate(2, 12)));
        assertTrue(zoneOfControl.contains(makeCoordinate(1, 12)));
        assertTrue(zoneOfControl.contains(makeCoordinate(1, 11)));
    }

    @Test
    public void testPlaceUnitWithTestGame(){
        testGame.putUnitAt(devin, 2, 2, SOUTH);
        testGame.putUnitAt(gamble, 3, 2, SOUTHEAST);
        testGame.putUnitAt(reynolds, 4, 2, NORTH);
        testGame.putUnitAt(heth, 2, 3, NORTH);
        testGame.putUnitAt(hill, 3, 3, NORTHWEST);

        assertTrue(testGame.getUnitsAt(GameCoordinate.makeCoordinate(2, 2)).contains(devin));
        assertTrue(testGame.getUnitsAt(GameCoordinate.makeCoordinate(3, 2)).contains(gamble));
        assertTrue(testGame.getUnitsAt(GameCoordinate.makeCoordinate(4, 2)).contains(reynolds));
        assertTrue(testGame.getUnitsAt(GameCoordinate.makeCoordinate(2, 3)).contains(heth));
        assertTrue(testGame.getUnitsAt(GameCoordinate.makeCoordinate(3, 3)).contains(hill));
    }

    @Test
    public void testClearBoard(){
        testGame.clearBoard();
        assertEquals(0, testGame.getBoard().units.size(), 0);
    }

    @Test
    public void testGetBattlesToResolveFromDiscussions(){
        testGame.clearBoard();
        testGame.putUnitAt(devin, 2, 2, SOUTH);
        testGame.putUnitAt(gamble, 3, 2, SOUTHEAST);
        testGame.putUnitAt(reynolds, 4, 2, NORTH);
        testGame.putUnitAt(heth, 3, 3, NORTH);
        testGame.putUnitAt(hill, 4, 3, NORTHWEST);

        Collection<BattleDescriptor> battles = testGame.getBattlesToResolve();
        BattleDescriptor battleA = Battle.makeEmptyBattle();
        battleA.getAttackers().addAll(Arrays.asList(devin, gamble, reynolds));
        battleA.getDefenders().add(heth);

        BattleDescriptor battleB = Battle.makeEmptyBattle();
        battleB.getAttackers().addAll(Arrays.asList(gamble, reynolds));
        battleB.getDefenders().add(hill);

        assertNotNull(battles);
        assertTrue(battles.contains(battleA));
        assertTrue(battles.contains(battleB));
    }

    //game sequence tests
    //movement
    @Test
    public void testUMOVEAtStart(){
        assertEquals(UMOVE, game.getCurrentStep());
    }

    @Test(expected = GbgInvalidMoveException.class)
    public void testAttemptToMoveUnitOfWrongColor(){
        game.moveUnit(heth, game.whereIsUnit(heth), GameCoordinate.makeCoordinate(1, 1));
    }

    @Test(expected = GbgInvalidMoveException.class)
    public void testAttemptToMoveUnitFromWrongLocation(){
        game.moveUnit(gamble, GameCoordinate.makeCoordinate(1, 1), GameCoordinate.makeCoordinate(2, 2));
    }

    @Test (expected = GbgInvalidMoveException.class)
    public void testAttemptToMoveUnitBeyondMovementFactor(){
        game.moveUnit(gamble, game.whereIsUnit(gamble), GameCoordinate.makeCoordinate(20, 2));
    }

    @Test
    public void testChangeFacing(){
        game.setUnitFacing(gamble, SOUTH);
        assertEquals(SOUTH, game.getUnitFacing(gamble));
    }

    @Test (expected = GbgInvalidMoveException.class)
    public void testChangeFacingTwiceInOneMove(){
        assertEquals(SOUTH, game.getUnitFacing(devin));
        game.setUnitFacing(devin, NORTH);
        assertEquals(NORTH, game.getUnitFacing(devin)); //should be fine till this point
        game.setUnitFacing(devin, NORTHEAST); //error thrown here
    }

    @Test
    public void testValidMove(){
        Coordinate dest = GameCoordinate.makeCoordinate(10, 12);
        game.moveUnit(gamble, game.whereIsUnit(gamble), dest );
        assertEquals(dest, game.whereIsUnit(gamble)); //move should happen
    }

    @Test (expected = GbgInvalidMoveException.class)
    public void testMoveTwiceInOneMove(){
        Coordinate dest = GameCoordinate.makeCoordinate(10, 12);
        game.moveUnit(gamble, game.whereIsUnit(gamble), dest );
        assertEquals(dest, game.whereIsUnit(gamble)); //move should happen

        game.moveUnit(gamble, dest, GameCoordinate.makeCoordinate(11, 11)); //invalid: already moved unit
    }

    @Test
    public void testNoStacksAfterFirstUnionMove(){
        game.endStep();
        assertEquals(UBATTLE, game.getCurrentStep());
        assertTrue(StepValidators.noStacksOnBoard.apply(GameState.makeState(game)));
    }

    @Test
    public void testMoveToZoneOfControl(){
        testGame.putUnitAt(heth, 10, 14, EAST);
        testGame.moveUnit(gamble, testGame.whereIsUnit(gamble), GameCoordinate.makeCoordinate(11, 15));
    }

    //@Test (expected = GbgInvalidMoveException.class)
    //todo: check if unit can be passed over?
    public void testMoveThroughZoneOfControl(){
        testGame.putUnitAt(heth, 10, 13, EAST);
        testGame.putUnitAt(hill, 12, 13, WEST);
        testGame.moveUnit(gamble, testGame.whereIsUnit(gamble), GameCoordinate.makeCoordinate(11, 14));
    }

    //battle testing
    @Test (expected = GbgInvalidActionException.class)
    public void testAttackWithDefendingUnit(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getAttackers().add(heth);
        battle.getDefenders().add(heth);
        game.resolveBattle(battle);
    }

    @Test (expected = GbgInvalidActionException.class)
    public void testAttackAttackingUnit(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getAttackers().add(gamble);
        battle.getDefenders().add(reynolds);
        game.resolveBattle(battle);
    }

    @Test
    public void testGambleAttackHeth(){
        testGame.putUnitAt(gamble, 5, 5, NORTH);
        testGame.putUnitAt(heth, 4, 4, SOUTHEAST);

        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getAttackers().add(gamble);
        battle.getDefenders().add(heth);

        testGame.setGameStep(UBATTLE);
        assertEquals(AELIM, testGame.resolveBattle(battle).getBattleResult());
    }

    @Test (expected = GbgInvalidActionException.class)
    public void testUnitParticipatingMoreThanOnce(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getAttackers().add(gamble);
        battle.getDefenders().add(heth);
        game.resolveBattle(battle);
        game.resolveBattle(battle);
    }

    @Test (expected = GbgInvalidActionException.class)
    public void testUnitAttacksItself(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getAttackers().add(gamble);
        battle.getDefenders().add(gamble);
        game.resolveBattle(battle);
    }

    @Test (expected = GbgInvalidActionException.class)
    public void testEmptyAttackers(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getDefenders().add(heth);
        game.resolveBattle(battle);
    }

    @Test (expected = GbgInvalidActionException.class)
    public void testEmptyDefenders(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        battle.getAttackers().add(gamble);
        game.resolveBattle(battle);
    }


    @Test (expected = GbgInvalidActionException.class)
    public void testEmptyAttackersAndDefenders(){
        BattleDescriptor battle = Battle.makeEmptyBattle();
        game.resolveBattle(battle);
    }

    @Test
    public void testBFS(){
        Coordinate src = GameCoordinate.makeCoordinate(11, 11);
        Coordinate dest = GameCoordinate.makeCoordinate(11, 15);
        BFS.evaluate(src);
        List<Coordinate> path = BFS.shortestPath(dest);

        assertNotNull(path);
        assertTrue(path.size() > 0);
        assertTrue(path.contains(src));
        assertTrue(path.contains(dest));
    }

    @Test
    public void testPathFinder(){
        Coordinate src = GameCoordinate.makeCoordinate(1, 1);
        Coordinate dest = GameCoordinate.makeCoordinate(3, 3);

        PathFinder finder = new PathFinder(GameState.makeState(game.getBoard(), game.getCurrentStep()));
        finder.run(src, dest, 3);
        finder.paths.forEach(System.out::println);
    }


    @Test
    public void testPathFinderWithContext(){
        Coordinate src = GameCoordinate.makeCoordinate(1, 1);
        Coordinate dest = GameCoordinate.makeCoordinate(3, 2);

        testGame.clearBoard();
        testGame.putUnitAt(gamble, 1, 1, NORTH);
        testGame.putUnitAt(heth, 3, 3, WEST);
        testGame.putUnitAt(hill, 1, 3, NORTH);
        PathFinder finder = new PathFinder(GameState.makeState(testGame.getBoard(), testGame.getCurrentStep()));
        finder.run(src, dest, 4);

        Collection<Coordinate> active = testGame.getBoard().getActiveZonesOfControl(testGame.getCurrentStep());
        //ensure no active zone in paths
        for(List<Coordinate> path : finder.paths){
            for(Coordinate where : path){
                if(active.contains(where))
                    assert(false);
            }
        }
    }

    /*
    @Test
    public void testBFSWithContext(){
        Coordinate src = GameCoordinate.makeCoordinate(11, 11);
        Coordinate dest = GameCoordinate.makeCoordinate(11, 15);
        GameState state = GameState.makeState(game.getBoard(), game.getCurrentStep());
        BFS.setSearchParams(src, dest, gamble.getMovementFactor());
        BFS.findPathWithContext(state, src, new Stack());

        assertNotNull(BFS.possiblePaths);
        assertTrue(BFS.possiblePaths.size() > 0);

        //ensure all possible paths contain start and end locations
        for(List<Coordinate> list : BFS.possiblePaths){
            assert list.contains(src) && list.contains(dest);
        }

        //ensure no path contains a square with a piece
        for(List<Coordinate> list : BFS.possiblePaths){
            for(Coordinate where : list){
                assert LocationValidators.noUnitsPresent.apply(state, where);
            }
        }

    }

    @Test
    public void testOnePathGambleMove(){
        Coordinate src = game.whereIsUnit(gamble);
        Coordinate dest =  GameCoordinate.makeCoordinate(11, 12);

        BFS.setSearchParams(src, dest, 1);
        BFS.findPath(src, new Stack());
        assertEquals(1, BFS.possiblePaths.size(), 0);

//        game.moveUnit(gamble, game.whereIsUnit(gamble),);
    }

    @Test
    public void testMutiplePathsGambleMove(){
        testGame.putUnitAt(gamble, 1, 1, EAST);
        Coordinate src = testGame.whereIsUnit(gamble);
        Coordinate dest =  GameCoordinate.makeCoordinate(1, 2);

        BFS.setSearchParams(src, dest, gamble.getMovementFactor());
        BFS.findPath(src, new Stack());
        assertEquals(24, BFS.possiblePaths.size(), 0); //test told me so

    }
*/
}
