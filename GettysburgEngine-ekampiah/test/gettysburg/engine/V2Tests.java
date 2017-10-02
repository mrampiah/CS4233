package gettysburg.engine;

import gettysburg.common.*;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import org.junit.Before;
import org.junit.Test;
import student.gettysburg.engine.common.*;
import student.gettysburg.engine.utility.configure.UnitInitializer;

import java.util.*;

import static gettysburg.common.ArmyID.CONFEDERATE;
import static gettysburg.common.ArmyID.UNION;
import static gettysburg.common.Direction.*;
import static gettysburg.common.UnitSize.BRIGADE;
import static gettysburg.common.UnitType.CAVALRY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static student.gettysburg.engine.GettysburgFactory.makeCoordinate;
import static student.gettysburg.engine.GettysburgFactory.makeGame;

public class V2Tests{
    //create units and add
    List<UnitInitializer> sampleConfig = new LinkedList<UnitInitializer>();

    protected GbgGame game;
    protected GbgUnit gamble, devin, heth;
    protected static boolean getUnit = false,
            endMoveStep = false,
            endBattleStep = false;

    @Before
    public void setup() {
        game = makeGame();
        try {
            gamble = game.getUnit("Gamble", UNION);
            devin = game.getUnit("Devin", UNION);
            heth = game.getUnit("Heth", CONFEDERATE);
        } catch (Exception e) {
            if (!getUnit) {
                getUnit = true;
            }
            gamble = game.getUnitsAt(makeCoordinate(11, 11)).iterator().next();
            devin = game.getUnitsAt(makeCoordinate(13, 9)).iterator().next();
            heth = game.getUnitsAt(makeCoordinate(8, 8)).iterator().next();
        }
        gamble.setFacing(WEST);
        devin.setFacing(SOUTH);
        heth.setFacing(EAST);

        //so above changes don't interfere with real tests
        ((GettysburgEngine) game).getBoard().resetFacingChanged();

//        sampleConfig.add(new UnitInitializer(0, 11, 11, ArmyID.UNION, 1, WEST, "Gamble", 4, BRIGADE, CAVALRY));//gamble
//        sampleConfig.add(new UnitInitializer(0, 13, 9, ArmyID.UNION, 1, SOUTH, "Devin", 4, BRIGADE, CAVALRY));//devin
//        sampleConfig.add(new UnitInitializer(0, 8, 8, ArmyID.CONFEDERATE, 1, EAST, "Heth", 4, BRIGADE, CAVALRY));//heth
    }

    @Test
    public void testValidAdjacent(){
        Coordinate c1 = CoordinateImpl.makeCoordinate(3, 3);

        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(3, 2)))); //N
        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(3, 4)))); //S
        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(4, 3)))); //E
        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(4, 2)))); //NE
        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(2, 2)))); //NW
        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(4, 4)))); //SE
        assertTrue(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(2, 4)))); //SW
    }


    @Test
    public void testInvalidAdjacent(){
        Coordinate c1 = CoordinateImpl.makeCoordinate(5, 9);

        assertFalse(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(2, 15)))); //L
        assertFalse(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(10, 2)))); //R
        assertFalse(RuleEngine.adjacent.test(new CoordinatePair(c1, CoordinateImpl.makeCoordinate(5, 11)))); //M
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
    public void bfsTest2(){
        List<Coordinate> result = GBoard.bfs(CoordinateImpl.makeCoordinate(1, 1),
                CoordinateImpl.makeCoordinate(5, 5));
//        result.forEach(System.out::println);

        //should only contain these coordinates
        assertTrue(result.size() == 8);
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(1, 1)));
        assertTrue(result.contains(CoordinateImpl.makeCoordinate(5, 5)));
    }
}