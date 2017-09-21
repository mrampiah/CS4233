package gettysburg.engine;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import gettysburg.common.ArmyID;
import gettysburg.common.Coordinate;
import gettysburg.common.Direction;
import gettysburg.common.GbgGame;
import gettysburg.common.GbgGameStep;
import gettysburg.common.GbgUnit;
import gettysburg.common.exceptions.GbgInvalidMoveException;
import student.gettysburg.engine.GettysburgFactory;
import student.gettysburg.engine.common.CoordinateImpl;

public class TestCases {
	private GbgGame game;
	private Coordinate gambleCoord, devinCoord, hethCoord;

	@Before
	public void setup() {
		game = GettysburgFactory.makeGame();
		gambleCoord = CoordinateImpl.makeCoordinate(11, 11);
		devinCoord = CoordinateImpl.makeCoordinate(13, 9);
		hethCoord = CoordinateImpl.makeCoordinate(8, 8);
	}

	@Test
	public void testGameNotNull() {
		assertNotNull(game);
	}

	@Test
	public void testGameStart() {
		assertEquals(1, game.getTurnNumber(), 0);
		assertEquals(GbgGameStep.UMOVE, game.getCurrentStep());
	}

	@Test
	public void testGambleConfig() {
		Collection<GbgUnit> units = game.getUnitsAt(gambleCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		assertEquals(ArmyID.UNION, unit.getArmy());
		assertEquals("Gamble", unit.getLeader());
		assertEquals(Direction.WEST, unit.getFacing());
	}

	@Test
	public void testDevinConfig() {
		Collection<GbgUnit> units = game.getUnitsAt(devinCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		assertEquals(ArmyID.UNION, unit.getArmy());
		assertEquals("Devin", unit.getLeader());
		assertEquals(Direction.SOUTH, unit.getFacing());
	}

	@Test
	public void testHethConfig() {
		Collection<GbgUnit> units = game.getUnitsAt(hethCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		assertEquals(ArmyID.CONFEDERATE, unit.getArmy());
		assertEquals("Heth", unit.getLeader());
		assertEquals(Direction.EAST, unit.getFacing());
	}

	@Test
	public void testWhereIsGamble() {
		assertEquals(gambleCoord, game.whereIsUnit("Gamble", ArmyID.UNION));
	}

	@Test
	public void testWhereIsDevin() {
		assertEquals(devinCoord, game.whereIsUnit("Devin", ArmyID.UNION));
	}

	@Test
	public void testWhereIsHeth() {
		assertEquals(hethCoord, game.whereIsUnit("Heth", ArmyID.CONFEDERATE));
	}

	@Test
	public void testGambleGetFacing() {
		// todo: for everyone
	}

	@Test
	public void testValidGambleMove1() {
		Collection<GbgUnit> units = game.getUnitsAt(gambleCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		Coordinate coord = CoordinateImpl.makeCoordinate(11, 10);
		game.moveUnit(unit, gambleCoord, coord);

		// check if it really moved
		units = game.getUnitsAt(gambleCoord);
		assertNotNull(units);
		assertEquals(0, units.size(), 0);

		units = game.getUnitsAt(coord);
		assertEquals(1, units.size(), 0);

		GbgUnit newUnit = units.iterator().next();
		assertEquals(newUnit, unit);
	}

	@Test
	public void testValidGambleMove2() {
		Collection<GbgUnit> units = game.getUnitsAt(gambleCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		Coordinate coord = CoordinateImpl.makeCoordinate(13, 11);
		game.moveUnit(unit, gambleCoord, coord);

		// check if it really moved
		units = game.getUnitsAt(gambleCoord);
		assertNotNull(units);
		assertEquals(0, units.size(), 0);

		units = game.getUnitsAt(coord);
		assertEquals(1, units.size(), 0);

		GbgUnit newUnit = units.iterator().next();
		assertEquals(newUnit, unit);
	}

//	@Test
	public void testValidDevinMove1() {
		Collection<GbgUnit> units = game.getUnitsAt(devinCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		Coordinate coord = CoordinateImpl.makeCoordinate(14, 8);
		game.moveUnit(unit, devinCoord, coord);

		// check if it really moved
		units = game.getUnitsAt(devinCoord);
		assertNotNull(units);
		assertEquals(0, units.size(), 0);

		units = game.getUnitsAt(coord);
		assertEquals(1, units.size(), 0);

		GbgUnit newUnit = units.iterator().next();
		assertEquals(newUnit, unit);
	}

//	@Test
	public void testValidDevinMove2() {
		Collection<GbgUnit> units = game.getUnitsAt(devinCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		Coordinate coord = CoordinateImpl.makeCoordinate(15, 11);
		game.moveUnit(unit, devinCoord, coord);

		// check if it really moved
		units = game.getUnitsAt(devinCoord);
		assertNotNull(units);
		assertEquals(0, units.size(), 0);

		units = game.getUnitsAt(coord);
		assertEquals(1, units.size(), 0);

		GbgUnit newUnit = units.iterator().next();
		assertEquals(newUnit, unit);
	}

	@Test
	public void testInvalidGambleMove() {
		Collection<GbgUnit> units = game.getUnitsAt(gambleCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		Coordinate coord = CoordinateImpl.makeCoordinate(11, 18);
		try {
			game.moveUnit(unit, gambleCoord, coord);
		} catch (GbgInvalidMoveException e) {
			// new coord should be empty
			units = game.getUnitsAt(coord);
			try {
				assertEquals(0, units.size(), 0);
			} catch (NullPointerException ex) {
				assert (true);
			}
		}

		units = game.getUnitsAt(gambleCoord);

		// should still be there
		assertEquals(1, units.size(), 0);

		GbgUnit newUnit = units.iterator().next();
		assertEquals(unit, newUnit);
	}

	@Test
	public void testInvalidDevinMove() {
		Collection<GbgUnit> units = game.getUnitsAt(devinCoord);
		assertEquals(1, units.size(), 0);

		GbgUnit unit = units.iterator().next();
		try {
			game.moveUnit(unit, devinCoord, devinCoord);
		} catch (GbgInvalidMoveException e) {
			assert (true);
		}

		units = game.getUnitsAt(gambleCoord);

		// should still be there
		assertEquals(1, units.size(), 0);
	}

	@Test
	public void testNorthDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 10);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 5);
		assertEquals(Direction.NORTH, coord1.directionTo(coord2));
	}

	@Test
	public void testNorthEastDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(4, 10);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 5);
		assertEquals(Direction.NORTHEAST, coord1.directionTo(coord2));
	}

	@Test
	public void testNorthWestDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 10);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(4, 5);
		assertEquals(Direction.NORTHWEST, coord1.directionTo(coord2));
	}
	
	@Test
	public void testSouthDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 10);
		assertEquals(Direction.SOUTH, coord1.directionTo(coord2));
	}


	@Test
	public void testSouthEastDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(4, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 10);
		assertEquals(Direction.SOUTHEAST, coord1.directionTo(coord2));
	}

	@Test
	public void testSouthWestDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(5, 10);
		assertEquals(Direction.SOUTHWEST, coord1.directionTo(coord2));
	}


	@Test
	public void testEastDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(10, 10);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 10);
		assertEquals(Direction.EAST, coord1.directionTo(coord2));
	}

	@Test
	public void testWestDirection() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(10, 5);
		assertEquals(Direction.WEST, coord1.directionTo(coord2));
	}

	
	@Test
	public void testHorizontalDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(5, 5);
		assertEquals(4, coord1.distanceTo(coord2));
	}

	@Test
	public void testNorthDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 10);
		assertEquals(5, coord1.distanceTo(coord2));
	}

	@Test
	public void testNorthEastDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(5, 7);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 5);
		assertEquals(4, coord1.distanceTo(coord2));
	}


	@Test
	public void testNorthWestDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 10);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(6, 7);
		assertEquals(5, coord1.distanceTo(coord2));
	}


	@Test
	public void testSouthEastDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(5, 1);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 7);
		assertEquals(6, coord1.distanceTo(coord2));
	}


	@Test
	public void testSouthWestDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 1);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(4, 7);
		assertEquals(6, coord1.distanceTo(coord2));
	}


	@Test
	public void testSouthDistance() {
		Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 7);
		assertEquals(2, coord1.distanceTo(coord2));
	}
	
	@Test
	public void testEastDistance() {
	Coordinate coord1 = CoordinateImpl.makeCoordinate(5, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(1, 5);
		assertEquals(4, coord1.distanceTo(coord2));
	}
	

	@Test
	public void testWestDistance() {
	Coordinate coord1 = CoordinateImpl.makeCoordinate(1, 5);
		Coordinate coord2 = CoordinateImpl.makeCoordinate(10, 5);
		assertEquals(9, coord1.distanceTo(coord2));
	}
}
