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

package ddd.hw;

/**
 * Interface that describes the required behavior of a door controller for use in
 * Doug's Dog Door sysgtem.
 * @version Jul 12, 2017
 */
public interface DoorController
{
	/**
	 * Open the door. If it is already open, do nothing; otherwise open the door.
	 */
	void open();
	
	/**
	 * Close the door. If it is already closed, do nothing; otherwise close the door.
	 */
	void close();
	
	/**
	 * @return true if the door is open, false otherwise.
	 */
	boolean isOpen();
}
