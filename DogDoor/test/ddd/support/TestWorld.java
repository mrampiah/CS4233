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

package ddd.support;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ddd.*;
import ddd.hw.TestDoorController;

/**
 * This class is used to keep the state of the world, that is all domain
 * objects in use for our tests.
 * @version Jul 13, 2017
 */
public class TestWorld
{
	private DogDoor theDoor;
	private Remote theRemote;
	
	public DogDoor getTheDoor()
	{
		if (theDoor == null)
			getNewDoor();
		return theDoor;
	}
	

	public DogDoor getNewDoor()
	{
		theDoor = new DogDoor(new TestDoorController());
		return theDoor;
	}
	
	public DogDoor getNewDoor(int remoteId) {
		theDoor = new DogDoor(new TestDoorController(), remoteId);
		return theDoor;
	}
	
	public Remote getTheRemote()
	{
		if (theRemote == null) 
			theRemote = new SimpleRemote(getTheDoor());
		
		return theRemote;
	}
	
	public Remote makeRemote(DogDoor door, int id) {
		return new RemoteWithId(door, id);
	}
	
	public void pressRemoteWithId(int id) {
		ArrayList<RemoteWithId> remotes = theDoor.getRemotes();
		List<Remote> result = remotes.stream().filter(r -> r.getId() == id).collect(Collectors.toList());
		
		if(result.size() > 0)
			result.get(0).press();
	}
}
