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
 * This is a test class that implements the DoorController interface. It is a
 * test double that is just used for testing.
 * @version Jul 13, 2017
 */
public class TestDoorController implements DoorController
{
	private boolean doorIsOpen;
	
	public TestDoorController()
	{
		doorIsOpen = false;
	}
	
	/*
	 * @see ddd.hw.DoorController#open()
	 */
	@Override
	public void open()
	{
		doorIsOpen = true;
	}

	/*
	 * @see ddd.hw.DoorController#close()
	 */
	@Override
	public void close()
	{
		doorIsOpen = false;
	}

	/*
	 * @see ddd.hw.DoorController#isOpen()
	 */
	@Override
	public boolean isOpen()
	{
		return doorIsOpen;
	}

}
