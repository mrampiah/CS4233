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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * This is the class that implements the 2D coordinate interface, but it is an
 * implementation of a hexagonal coordinate and has several methods to support
 * operations that would be appropriate for a board game implementation.
 * </p>
 * <p>
 * I have created two fields for the x and y-coordinates in order to generate
 * the <tt>equals()</tt> and <tt>hashCode()</tt> methods. This may be useful for
 * the copy constructor creation method and are generated. I also created a
 * <tt>Direction</tt> enumeration to identify the six directions that would be
 * used on a hexagonal board.
 * </p>
 * <p>
 * Students should implement all methods that have empty bodies. There are
 * comments on each of these which specify the requirements for the method. All
 * code will be written using the TDD cycle described in the course module on
 * TDD. Look for "TODO:" comments.
 * </p>
 * 
 * @see <a href=
 *      "http://www.vbforums.com/showthread.php?663283-Hexagonal-coordinate-system">
 *      Hexagonal Coordinate System</a>
 * @see <a href=
 *      "http://keekerdc.com/2011/03/hexagon-grids-coordinate-systems-and-distance-calculations/">
 *      Hexagon grids: coordinate systems and distance calculations</a>
 * @version Aug 20, 2017
 */
public class HexCoordinate implements Coordinate {
	private final int x;
	private final int y;

	// Way of classifying the six directions of neighbors.
	public enum Direction {
		N, NNE, SSE, S, SSW, NNW
	};

	private HexCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creation method when given the x and y-coordinates.
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @return the HexCoordinate instance corresponsing to (x, y)
	 */
	public static HexCoordinate makeCoordinate(int x, int y) {
		return new HexCoordinate(x, y);
	}

	/**
	 * Copy constructor creation method.
	 * 
	 * @param coordinate
	 *            a Coordinate that may not be a HexCoordinate
	 * @return the corresponding HexCoordinate or null if coord is null
	 */
	public static HexCoordinate makeCoordinate(Coordinate coordinate) {
		return new HexCoordinate(coordinate.getX(), coordinate.getY());
	}

	/**
	 * Compute the distance from this coordinate to a given coordinate. There is no
	 * guarantee that the other coordinate is a HexCoordinate instance.
	 * 
	 * @param other
	 *            the other coordinate
	 * @return the distance from this to the other coordinate
	 */
	public int distanceTo(Coordinate other) {
		try {
			return Math.max(Math.abs(x - other.getX()), Math.abs(y - other.getY()));
		} catch (NullPointerException e) {
			throw new NullPointerException(e.getMessage());
		}
	}

	/**
	 * <p>
	 * Given another coordinate, return the HexCoordinates of the hexes (2 of them)
	 * that are adjacent to the other coordinate and this coordinate. There is no
	 * guarantee that the other coordinate is a HexCoordinate instance.
	 * </p>
	 * <p>
	 * If there are not two common neighbors, return null.
	 * </p>
	 * 
	 * @param other
	 *            the other coordinate
	 * @return the array of common neighbors or null
	 */
	public HexCoordinate[] getCommonNeighbors(Coordinate other) {
		List<HexCoordinate> myNeighbors = Arrays.asList(this.getNeighbors());
		List<HexCoordinate> otherNeighbors = Arrays.asList(HexCoordinate.makeCoordinate(other).getNeighbors());

		List<HexCoordinate> result = new ArrayList<HexCoordinate>();
		for (HexCoordinate coord : myNeighbors) {
			if (otherNeighbors.contains(coord))
				result.add(coord);
		}
		return result.toArray(new HexCoordinate[result.size()]);
	}

	/**
	 * Determine if this coordinate is adjacent to the specified coordinate. There
	 * is no guarantee that the other coordinate is a HexCoordinate instance.
	 * 
	 * @param other
	 *            the coordinate to check for adjacenty to this coordinate
	 * @return true if other is adjacent to this coordinate, false otherwise
	 */
	public boolean isAdjacentTo(Coordinate other) {
		// check if same point, or distance too far
		if (this.equals(other) || this.distanceTo(other) > 1)
			return false;

		if (y == other.getY())
			return Math.abs(x - other.getX()) == 1;

		if (x == other.getX())
			return Math.abs(y - other.getY()) == 1;

		else {
			int horizontal = x - other.getX();
			int vertical = y - other.getY();
			return (horizontal > 0 && vertical < 0) || (vertical > 0 && horizontal < 0);
		}
	}

	/**
	 * Determine the direction of the parameter coordinate to this coordinate if it
	 * is in a straight line. If it not in a straight line, this method returns
	 * null. There is no guarantee that the other coordinate is a HexCoordinate
	 * instance.
	 * 
	 * @param other
	 * @return the direction of other relative to this or null if not in a straight
	 *         line
	 */
	public Direction getDirectionTo(Coordinate other) {
		if (!(other instanceof HexCoordinate))
			return null;

		if (x == other.getX()) {
			if (y > other.getY())
				return Direction.N;
			else
				return Direction.S;
		}
		
		if(x - other.getX() == 1) {
			if(y == other.getY())
				return Direction.NNE;
			else
				return Direction.SSE;
		}
		
		if(x - other.getX() == -1) {
			if(y == other.getY()) 
				return Direction.SSW;
			else
				return Direction.NNW;
		}
		
		throw new IllegalArgumentException("Argument is either null or isn't a valid hex coordinate");
	}

	/**
	 * @return an array containing all of the coordinates (6 of them) that are
	 *         adjacent to this coordinate.
	 */
	public HexCoordinate[] getNeighbors() {
		HexCoordinate list[] = new HexCoordinate[6];
		list[0] = HexCoordinate.makeCoordinate(this.x - 1, this.y);
		list[1] = HexCoordinate.makeCoordinate(this.x - 1, this.y + 1);
		list[2] = HexCoordinate.makeCoordinate(this.x, this.y + 1);
		list[3] = HexCoordinate.makeCoordinate(this.x + 1, this.y);
		list[4] = HexCoordinate.makeCoordinate(this.x + 1, this.y - 1);
		list[5] = HexCoordinate.makeCoordinate(this.x, this.y - 1);

		return list;
	}

	/*
	 * @see hexcoordinate.Coordinate#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/*
	 * @see hexcoordinate.Coordinate#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HexCoordinate other = (HexCoordinate) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

}
