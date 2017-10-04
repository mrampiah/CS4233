package tests;

import static model.GameCoordinate.makeCoordinate;
import static org.junit.Assert.*;

import engine.GameEngine;
import engine.TestGameEngine;
import gettysburg.common.Coordinate;
import gettysburg.common.exceptions.GbgInvalidCoordinateException;
import model.GameCoordinate;
import model.Unit;
import org.junit.Before;
import org.junit.Test;

import engine.Factory;
import gettysburg.common.GbgGame;
import util.BattleOrder;

import java.util.List;

public class V1Test {
    GameEngine game;
    TestGameEngine testGame;
    Unit heth, gamble, devin, reynolds, wadsworth, robinson, rowley;

    @Before
    public void setup() {
        game = (GameEngine) Factory.makeGame();
        testGame = Factory.makeTestGame();

        //initialize

        //union
        gamble = BattleOrder.getUnionBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Gamble")).findFirst().get().unit;
        devin = BattleOrder.getUnionBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Devin")).findFirst().get().unit;
        reynolds = BattleOrder.getUnionBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Reynolds")).findFirst().get().unit;
        wadsworth = BattleOrder.getUnionBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Wadsworth")).findFirst().get().unit;
        robinson = BattleOrder.getUnionBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Robinson")).findFirst().get().unit;
        rowley = BattleOrder.getUnionBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Rowley")).findFirst().get().unit;

        //confed
        heth = BattleOrder.getConfederateBattleOrder().stream()
                .filter(unit -> unit.unit.getLeader().equals("Heth")).findFirst().get().unit;

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
        List<Coordinate> neighbors = where.getNeighbors();

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
        List<Coordinate> neighbors = where.getNeighbors();

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
        testGame.placeUnit(src, heth);
    }

}
