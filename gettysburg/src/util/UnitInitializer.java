/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package util;

import gettysburg.common.*;
import model.GameCoordinate;
import model.Unit;

/**
 * A simple class with a single static method that returns the order of battle for
 * each side. It is to be used in initializing the game.
 * 
 * @version Jul 2, 2017
 */
public class UnitInitializer
{
	public int turn;
	public Coordinate where;
	public Unit unit;
	
	/**
	 * Default constructor.
	 */
	public UnitInitializer()
	{
		turn = 0;
		where = null;
		unit = null;
	}
	
	public UnitInitializer(int turn, int x, int y, ArmyID id, int combatFactor, 
			Direction facing, String leader, int movementFactor,
			UnitSize unitSize, UnitType unitType)
	{
		this.turn = turn;
		this.where = GameCoordinate.makeCoordinate(x, y);
		this.unit = Unit.makeUnit(id, combatFactor, movementFactor, facing, leader,
				unitSize, unitType);
	}
	
	public UnitInitializer(int turn, int x, int y, GbgUnit unit) {
		this(turn, x, y, unit.getArmy(), unit.getCombatFactor(), unit.getFacing(), unit.getLeader(),
				unit.getMovementFactor(), unit.getSize(), unit.getType());
	}
}
