package gettysburg.engine;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidActionException;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import gettysburg.engine.common.TestGettysburgEngine;
import org.junit.Before;
import org.junit.Test;
import student.gettysburg.engine.common.BFS;
import student.gettysburg.engine.common.CoordinateImpl;
import student.gettysburg.engine.common.GettysburgEngine;
import student.gettysburg.engine.common.RuleEngine;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static gettysburg.common.ArmyID.CONFEDERATE;
import static gettysburg.common.ArmyID.UNION;
import static gettysburg.common.Direction.*;
import static gettysburg.common.GbgGameStep.UBATTLE;
import static gettysburg.common.GbgGameStep.UMOVE;
import static org.junit.Assert.*;
import static student.gettysburg.engine.GettysburgFactory.makeCoordinate;
import static student.gettysburg.engine.GettysburgFactory.makeGame;

public class V2Tests{
    protected GbgGame game;
    protected GbgUnit gamble, devin, heth;
    protected static boolean getUnit = false,
            endMoveStep = false,
            endBattleStep = false;
    protected TestGettysburgEngine testDouble;

    @Before
    public void setup() {
        game = makeGame();
        testDouble = new TestGettysburgEngine((GettysburgEngine) game);
        testDouble.clearBoard();

        try {
            gamble = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
            devin = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
            heth = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
        } catch (Exception e) {
            if (!getUnit) {
                getUnit = true;
            }
        }
        gamble.setFacing(WEST);
        devin.setFacing(SOUTH);
        heth.setFacing(EAST);

        //so above changes don't interfere with real tests
        ((GettysburgEngine) game).getBoard().resetFacingChanged();
    }

    @Test
    public void testGambleLocation(){
        try {
            GbgUnit gambleUnit = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
        }catch(NullPointerException ex){
            fail();
        }
    }


    @Test
    public void testDevinLocation(){
        GbgUnit devinUnit = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
    }

    @Test
    public void testHethLocation(){
        GbgUnit hethUnit = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
    }

    @Test
    public void testValidAdjacent(){
        Coordinate c1 = CoordinateImpl.makeCoordinate(3, 3);

        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(3, 2))); //N
        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(3, 4))); //S
        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(4, 3))); //E
        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(4, 2))); //NE
        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(2, 2))); //NW
        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(4, 4))); //SE
        assertTrue(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(2, 4))); //SW
    }


    @Test
    public void testInvalidAdjacent(){
        Coordinate c1 = CoordinateImpl.makeCoordinate(5, 9);

        assertFalse(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(2, 15))); //L
        assertFalse(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(10, 2))); //R
        assertFalse(RuleEngine.adjacent.test(c1, CoordinateImpl.makeCoordinate(5, 11))); //M
    }


    @Test
    public void testZoneOfControlForDevinFacingSouth(){
        assertNotNull(devin);
        Collection<Coordinate> zone = ((GettysburgEngine) game).getZoneOfControl(devin);
        assertTrue(zone.size() == 3);
        assertTrue(zone.contains(CoordinateImpl.makeCoordinate(12, 10)));
        assertTrue(zone.contains(CoordinateImpl.makeCoordinate(13, 10)));
        assertTrue(zone.contains(CoordinateImpl.makeCoordinate(14, 10)));

        //examples of fail cases
        assertFalse(zone.contains(CoordinateImpl.makeCoordinate(1, 3)));
        assertFalse(zone.contains(CoordinateImpl.makeCoordinate(13, 9)));
        assertFalse(zone.contains(CoordinateImpl.makeCoordinate(12, 9)));
    }

    @Test
    public void testZoneOfControlForHethFacingEast(){
        assertNotNull(heth);
        Collection<Coordinate> zone = ((GettysburgEngine) game).getZoneOfControl(heth);
        assertTrue(zone.size() == 3);
        assertTrue(zone.contains(CoordinateImpl.makeCoordinate(9, 7)));
        assertTrue(zone.contains(CoordinateImpl.makeCoordinate(9, 8)));
        assertTrue(zone.contains(CoordinateImpl.makeCoordinate(9, 9)));

        //examples of fail cases
        assertFalse(zone.contains(CoordinateImpl.makeCoordinate(8, 8)));
        assertFalse(zone.contains(CoordinateImpl.makeCoordinate(8, 9)));
        assertFalse(zone.contains(CoordinateImpl.makeCoordinate(7, 8)));
    }

    //graph tests
    @Test
    public void testDijkstra(){
        Coordinate src = CoordinateImpl.makeCoordinate(13, 9);
        Coordinate dest = CoordinateImpl.makeCoordinate(9, 13);

        BFS.evaluate(src, game);
        List<Coordinate> result = BFS.shortestPath(dest);
        assertNotNull(result);
        assertEquals(5, result.size(), 0);
        assertTrue(result.contains(src));
        assertTrue(result.contains(dest));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(12, 10)));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(11, 11)));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(10, 12)));
    }


    @Test
    public void dijkstraTest2(){
        Coordinate src = CoordinateImpl.makeCoordinate(1, 1);
        Coordinate dest = CoordinateImpl.makeCoordinate(5, 5);

        BFS.evaluate(src, game);

        List<Coordinate> result = BFS.shortestPath(dest);
//        result.forEach(System.out::println);

        //should only contain these coordinates
        assertNotNull(result);
        assertTrue(result.size() == 5);
        assertTrue(result.contains(src));
        assertTrue(result.contains(dest));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(2, 2)));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(3, 3)));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(4, 4)));
    }

    public void testValidUnionMove(){
        Coordinate newAd = CoordinateImpl.makeCoordinate(10, 8);
        Coordinate oldAd =  game.whereIsUnit(gamble);
        runMove(gamble, oldAd, newAd);

        assertNull(game.getUnitsAt(oldAd));
        assertNotNull(game.getUnitsAt(newAd));
        assertEquals(1, game.getTurnNumber(), 0); //should sill be turn 1
        assertEquals(UBATTLE, game.getCurrentStep());
    }

    @Test
    public void testInvalidUnionMove(){
        Coordinate newAd = CoordinateImpl.makeCoordinate(10, 3);
        Coordinate oldAd =  game.whereIsUnit(gamble);
        try {
            runMove(gamble, oldAd, newAd);
        }catch (GbgInvalidMoveException ex){
            assert(true);
        }
        assertNotNull(game.getUnitsAt(oldAd));
        assertTrue(game.getUnitsAt(newAd) == null || !game.getUnitsAt(newAd).contains(gamble));
        assertEquals(1, game.getTurnNumber(), 0); //should sill be turn 1
        assertEquals(UMOVE, game.getCurrentStep()); //should still be union move
    }

    public void testNoStacksAfterMove(){
        Coordinate newAd = CoordinateImpl.makeCoordinate(9, 28);
        Coordinate oldAd =  CoordinateImpl.makeCoordinate(7, 28);

        Set<GbgUnit> units = (Set) game.getUnitsAt(oldAd);
        GbgUnit unit = units.iterator().next();
        assertEquals(4, units.size(), 0);

        runMove(unit, oldAd, newAd);

        //none at old place now
        assertNull(game.getUnitsAt(oldAd));
        assertEquals(1, game.getUnitsAt(newAd).size());

        //unit at new ad
        assertTrue(game.getUnitsAt(newAd).contains(unit));
    }

    @Test
    public void turnIncreasesAfterValidSteps(){
        try {
            game.endStep(); //umove
            game.getBattlesToResolve();
            game.endStep(); //ubattle
            game.endStep(); //cmove
            game.getBattlesToResolve();
            game.endStep(); //cbattle
        }catch(GbgInvalidMoveException ex){
            fail();
        }
        assertEquals(2, game.getTurnNumber(), 0);
    }

    @Test
    public void testConfederateTurn(){
        game.endStep(); //umove
        game.getBattlesToResolve();
        game.endStep(); //ubattle

        assertEquals(GbgGameStep.CMOVE, game.getCurrentStep());
    }

    @Test
    public void testGetAllUnionUnits(){
        List<GbgUnit> units = ((GettysburgEngine)game).getAllUnits(UNION);
        assertEquals(6, units.size(), 0); //at start of game 6 union units
    }

    @Test
    public void testGetAllConfederateUnits(){
        List<GbgUnit> units = ((GettysburgEngine)game).getAllUnits(CONFEDERATE);
        assertEquals(1, units.size(), 0); //at start of game 1 union units
    }

    @Test
    public void testPutUnitAt(){
        testDouble.putUnitAt(gamble, 2, 2, SOUTH);
        Collection<GbgUnit> units = testDouble.getUnitsAt(CoordinateImpl.makeCoordinate(2, 2));
        assertTrue(units.contains(gamble));
    }

    @Test
    public void testSetFacing(){
        testDouble.putUnitAt(gamble, 2, 2, SOUTH);
        Collection<GbgUnit> units = testDouble.getUnitsAt(CoordinateImpl.makeCoordinate(2, 2));
        assertTrue(units.contains(gamble));
        assertEquals(SOUTH, units.iterator().next().getFacing());
    }

    @Test(expected = GbgInvalidActionException.class)
    public void noCallToGetBattlesToResolveAfterMoveThrowsException(){
        testDouble.endStep();
        testDouble.endStep();
    }

    @Test
    public void testBattle(){
	    testDouble.setRuleEngine(testDouble);
        //place units within zones of control
        testDouble.putUnitAt(gamble, 2, 2, SOUTH);
        testDouble.putUnitAt(heth, 1, 3, EAST);
        testDouble.endStep();
        assertTrue(testDouble.getUnitsAt(CoordinateImpl.makeCoordinate(2, 2)).contains(gamble));
        assertTrue(testDouble.getUnitsAt(CoordinateImpl.makeCoordinate(1, 3)).contains(heth));
        assertTrue(testDouble.getZoneOfControl(gamble).contains(CoordinateImpl.makeCoordinate(1, 3)));

        Collection<BattleDescriptor> battles = testDouble.getBattlesToResolve();
        assertEquals(1, battles.size(), 0);
    }

    private void runMove(GbgUnit unit, Coordinate oldAd, Coordinate newAd) throws GbgInvalidMoveException{
        game.moveUnit(unit, oldAd, newAd);
        game.endStep();
    }
}