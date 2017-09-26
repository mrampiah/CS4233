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

import java.util.*;
import ddd.hw.DoorController;

/**
 * The implementation of the DoorController for 
 * @version Jul 12, 2017
 */
/**
 * Description
 * @version Jul 13, 2017
 */
public class DogDoor
{
	private final DoorController controller;
	private int closeDelay;
	private Timer t;
	private ArrayList<RemoteWithId> remotes;
	
	/**
	 * Default constructor.
	 * @param controller
	 */
	public DogDoor(DoorController controller)
	{
		this.controller = controller;
		closeDelay = 5; //default to 5s
		setupTimer();
		remotes = new ArrayList<RemoteWithId>();
	}
	
	public DogDoor(DoorController controller, Integer remoteId) {
		this(controller);
		addRemote(new RemoteWithId(this, remoteId));
	}
	
	/**
	 * @return true if the door is open and false otherwise
	 */
	public boolean isOpen()
	{
		return controller.isOpen();
	}
	
	/**
	 * The remote button has been pressed, take the appropriate action.
	 */
	public void press()
	{
		if(!isTimerEnabled()) {
			setupTimer();
			
			if(isOpen())
				controller.close();
			
			return;
		}
		
		if (isOpen()) {
			t.cancel();
			t = null; //disable timer if door is already open
		} else {
			controller.open();
			if(closeDelay == 0)
				return;			
		    t.schedule(new TimerTask() {
		    		public void run() { controller.close(); t.cancel(); }
		    }, closeDelay * 1000);
		}
	}
	
	/*
	 * Method to handle remotes with fixed id's. 
	 * Check if this door has been paired with remote with given id and act accordingly
	 */
	public void press(int remoteId) {
		if(remotes.stream().anyMatch(r -> r.getId() == remoteId))
			press();
	}
	
	/*
	 * Initialize the timer for later use
	 */
	private void setupTimer() {
		t = new Timer();
	}
	
	public void setCloseDelay(int seconds) {
		if(seconds < 0) {
			closeDelay = 5;
			throw new RuntimeException("Default close time must be >= 0");
		}
		
		closeDelay = seconds;
	}
	
	public int getCloseDelay() {
		return closeDelay;
	}
	
	public boolean isTimerEnabled() {
		return t != null;
	}
	
	public int addRemote(Remote remote) {
		remotes.add((RemoteWithId)remote);
		return remotes.size() - 1;
	}
	
	public ArrayList<RemoteWithId> getRemotes(){
		return remotes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((remotes == null) ? 0 : remotes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DogDoor)
			return controller.equals(((DogDoor) obj).controller) && remotes.equals(((DogDoor) obj).remotes);
		
		return false;
	}
	
	
}
