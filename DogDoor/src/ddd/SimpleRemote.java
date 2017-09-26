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

package ddd;

/**
 * Class that represents a first version remote control. When the physical remote controller
 * has its button pressed, it calls the press() method in this class.
 * @version Jul 12, 2017
 */
public class SimpleRemote implements Remote
{
	public final DogDoor theDoor;
	
	/**
	 * Default constructor
	 * @param theDoor
	 */
	public SimpleRemote(DogDoor theDoor)
	{
		this.theDoor = theDoor;
	}

	/*
	 * @see ddd.Remote#press()
	 */
	@Override
	public void press()
	{
		theDoor.press();
		
	}
}
