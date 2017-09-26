/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package hexcoordinate;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import hexcoordinate.HexCoordinate.Direction;

/**
 * All unit tests will go in this file. You do not need to put comments on
 * your test methods. Their names should be sufficient to indicate their purpose.
 * 
 * @version Aug 20, 2017
 */
public class HexCoordinateTest
{
	

	@Test
	public void testMakeCoordinateFromPoint() {
		HexCoordinate hex = HexCoordinate.makeCoordinate(1,  2);
		assertNotNull(hex);
		assertEquals(1, hex.getX(), 0);
		assertEquals(2, hex.getY(), 0);
	}
	
	@Test
	public void testMakeCoordinateFromInterface() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(3, 4);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(coord1);
		assertEquals(coord1.getX(), coord2.getX());
		assertEquals(coord1.getY(), coord2.getY());
		assertTrue(coord2 instanceof HexCoordinate);
	}
	
	@Test
	public void testDistanceToSamePoint() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(3, 4);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(coord1);
		assertEquals(0, coord1.distanceTo(coord2));
	}
	
	@Test
	public void testDistanceToXPlusOne() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(-1, 1);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, 1);
		assertEquals(1, coord1.distanceTo(coord2), 0);
	}
	
	@Test
	public void testDistanceToYPlusOne() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(1, 1);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, 2);
		assertEquals(1, coord1.distanceTo(coord2), 0);
	}
	
	@Test
	public void testNorthDistance() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(3, 4);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(coord1.getX(), coord1.getY() + 4);
		assertEquals(4, coord1.distanceTo(coord2), 0);
	}
	
	@Test
	public void testDiagonalPositiveDistance() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(-1, -1);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(2, -1);
		assertEquals(3, coord1.distanceTo(coord2), 0);
	}
	
	@Test
	public void testDiagonalNegativeDistance() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(1, 1);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(-1, 2);
		assertEquals(2, coord1.distanceTo(coord2), 0);
	}
	
	@Test
	public void testIsAdjacentToSamePoint() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(3, 4);
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(coord1);
		assertFalse(coord1.isAdjacentTo(coord2));		
	}
	
	@Test
	public void testIsAdjacentToValidPoints() {
		HexCoordinate coord0 = HexCoordinate.makeCoordinate(0,0); //central	
		
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(-1, 1); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, 1);
		HexCoordinate coord3 = HexCoordinate.makeCoordinate(1, 0);
		HexCoordinate coord4 = HexCoordinate.makeCoordinate(1, -1);
		HexCoordinate coord5 = HexCoordinate.makeCoordinate(0, -1);
		HexCoordinate coord6 = HexCoordinate.makeCoordinate(-1, 0);

		assertTrue(coord0.isAdjacentTo(coord1));
		assertTrue(coord0.isAdjacentTo(coord2));
		assertTrue(coord0.isAdjacentTo(coord3));
		assertTrue(coord0.isAdjacentTo(coord4));
		assertTrue(coord0.isAdjacentTo(coord5));
		assertTrue(coord0.isAdjacentTo(coord6));
	}

	@Test
	public void testIsAdjacentToInvalidPoints() {
		HexCoordinate coord0 = HexCoordinate.makeCoordinate(0, 0); //central	
		
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(-2, 2); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(1, 1);
		HexCoordinate coord3 = HexCoordinate.makeCoordinate(2, -2);

		assertFalse(coord0.isAdjacentTo(coord1));
		assertFalse(coord0.isAdjacentTo(coord2));
		assertFalse(coord0.isAdjacentTo(coord3));
	}
	
	@Test
	public void testGetNeighbors() {
		HexCoordinate coord0 = HexCoordinate.makeCoordinate(0, 0); //central
		
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(-1, 1); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, 1);
		HexCoordinate coord3 = HexCoordinate.makeCoordinate(1, 0);
		HexCoordinate coord4 = HexCoordinate.makeCoordinate(1, -1);
		HexCoordinate coord5 = HexCoordinate.makeCoordinate(0, -1);
		HexCoordinate coord6 = HexCoordinate.makeCoordinate(-1, 0);
		
		List<HexCoordinate> list = Arrays.asList(coord0.getNeighbors());
		assertEquals(6, list.size(), 0);
		assertTrue(list.contains(coord1));
		assertTrue(list.contains(coord2));
		assertTrue(list.contains(coord3));
		assertTrue(list.contains(coord4));
		assertTrue(list.contains(coord5));
		assertTrue(list.contains(coord6));		
	}
	
	@Test
	public void testGetCommonNeighorsNone() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(-1, 1); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(2, -2);
		
		HexCoordinate[] neighbors = coord1.getCommonNeighbors(coord2);
		assertTrue(neighbors.length == 0);
	}

	@Test
	public void testGetCommonNeighorsOne() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(0, 0); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, -2);
		
		HexCoordinate[] neighbors = coord1.getCommonNeighbors(coord2);
		assertTrue(neighbors.length == 1);
		assertEquals(neighbors[0], HexCoordinate.makeCoordinate(0, -1));
	}

	@Test
	public void testGetCommonNeighorsTwo() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(1, -1); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(1, 0);
		
		List<HexCoordinate> neighbors = Arrays.asList(coord1.getCommonNeighbors(coord2));
		assertTrue(neighbors.size() == 2);
		assertTrue(neighbors.contains(HexCoordinate.makeCoordinate(0, 0)));
		assertTrue(neighbors.contains(HexCoordinate.makeCoordinate(2, -1)));
	}
	
	@Test
	public void testDirectionToNorth() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(0, 2); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, -2);
		assertEquals(Direction.N, coord1.getDirectionTo(coord2));
	}
	

	@Test
	public void testDirectionToSouth() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(0, -3); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, -1);
		assertEquals(Direction.S, coord1.getDirectionTo(coord2));
	}

	@Test
	public void testDirectionToNNE() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(1, -2); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(0, -2);
		assertEquals(Direction.NNE, coord1.getDirectionTo(coord2));
	}

	@Test
	public void testDirectionToNNW() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(0, -1); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(1, -2);
		assertEquals(Direction.NNW, coord1.getDirectionTo(coord2));
	}
	

	@Test
	public void testDirectionToSSE() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(0, 0); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(-1, 1);
		assertEquals(Direction.SSE, coord1.getDirectionTo(coord2));
	}

	@Test
	public void testDirectionToSSW() {
		HexCoordinate coord1 = HexCoordinate.makeCoordinate(1, 0); 
		HexCoordinate coord2 = HexCoordinate.makeCoordinate(2, 0);
		assertEquals(Direction.SSW, coord1.getDirectionTo(coord2));
	}
	
}
