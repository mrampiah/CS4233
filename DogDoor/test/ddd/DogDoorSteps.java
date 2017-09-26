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
package ddd;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cucumber.api.java8.En;
import ddd.support.TestWorld;

/**
 * Steps for the DogDoor features
 * @version Jul 11, 2017
 */
public class DogDoorSteps implements En
{
	private TestWorld world;
	private List<RemoteWithId> remotes;

	public DogDoorSteps()
	{
		world = new TestWorld();	
		remotes = new ArrayList<RemoteWithId>();

		// version 1 steps
		When("^the door is initialized$", () -> {
			world.getNewDoor();
		});

		Then("^the door is closed$", () -> {
			assertFalse(world.getTheDoor().isOpen());
		});

		When("^I press the remote button$", () -> {
			world.getTheRemote().press();
			assertTrue(true);
		});

		Then("^the door is open$", () -> {
			assertTrue(world.getTheDoor().isOpen());
		});

		Then("^after (\\d+) seconds the door closes$", (Integer delay) -> {
			try {
				Thread.sleep(delay * 1000 + 50);
			} catch (InterruptedException e) {
				fail("Sleep interrupted");
			}
		});

		Given("^that the door is closed$", () -> {
			world.getNewDoor();
			assertFalse(world.getTheDoor().isOpen());
		});

		Given("^that the door is open$", () -> {
			final DogDoor dd = world.getTheDoor();
			if (!dd.isOpen()) {
				dd.press();
			}
			assertTrue(dd.isOpen());
		});

		/***************** Version 2 steps ******************/
		Given("^that I have a dog door system$", () -> {
			world.getNewDoor();
			assertFalse(world.getTheDoor().isOpen());
		});

		When("^I change the default close time to (\\d+)$", (Integer closeDelay) -> {
			world.getTheDoor().setCloseDelay(closeDelay);
			assertEquals(closeDelay.intValue(), world.getTheDoor().getCloseDelay(), 0);
		});

		Then("^after (\\d+) seconds the door is open$", (Integer delay) -> {
			try {
				Thread.sleep(delay * 1000);
			} catch (InterruptedException e) {
				fail("Sleep interrupted");
			}

			assertTrue(world.getTheDoor().isOpen());			
		});

		When("^I change the default close time to -(\\d+)$", (Integer closeDelay) -> {
			try {
				world.getTheDoor().setCloseDelay(-closeDelay);
			}catch(RuntimeException e) {
				assertNotEquals(closeDelay, world.getTheDoor().getCloseDelay(), 0);
			}
		});

		Then("^an exception is thrown with the message \"([^\"]*)\"$", (String msg) -> {
			try {
				world.getTheDoor().setCloseDelay(-1);
			}catch(RuntimeException e) {
				assertEquals(msg, e.getMessage());
			}
		});

		Then("^the default close time is (\\d+) seconds$", (Integer delay) -> {
			assertEquals(5, world.getTheDoor().getCloseDelay(), 0);
		});

		When("^I press the remote button before the door closes automatically$", () -> {
			assertTrue(world.getTheDoor().isOpen());
			world.getTheRemote().press();

			assertTrue(true);
		});

		Then("^the timer is disabled$", () -> {
			assertFalse(world.getTheDoor().isTimerEnabled());
		});

		Then("^the door remains open$", () -> {
			assertTrue(world.getTheDoor().isOpen());
		});

		Given("^that the timer is disabled$", () -> {
			assertFalse(world.getTheDoor().isTimerEnabled());
		});

		Then("^the timer is enabled for the next button press$", () -> {
			assertTrue(world.getTheDoor().isTimerEnabled());
		});

		/***************** Version 3 steps ******************/
		Given("^that I have a dog door that accepts multiple remotes$", () -> {
			world.getNewDoor();
			assertTrue(true);
		});

		Given("^the door is initialized with remote (\\d+)$", (Integer arg1) -> {
			DogDoor door = world.getNewDoor(arg1);
			assertEquals(door, world.getTheDoor());
			ArrayList<RemoteWithId> remotes = door.getRemotes();

			assertTrue(remotes.size() > 0);
			assertEquals(arg1, remotes.get(0).getId(), 0);			
		});

		When("^I press the button on remote (\\d+)$", (Integer arg1) -> {
			world.pressRemoteWithId(arg1);
			assertTrue(true);
		});

		Given("^my neighbor has a remote with ID = (\\d+)$", (Integer arg1) -> {
			RemoteWithId neighbor = (RemoteWithId) world.makeRemote(world.getNewDoor(), arg1);
			remotes.add(neighbor);
			assertEquals(arg1, neighbor.getId(), 0);
		});

		When("^my neighbor presses the button on remote (\\d+)$", (Integer arg1) -> {
			getRemoteWithId(arg1).press();
			assertTrue(true);
		});

		Given("^I add remote (\\d+) to the remotes the door accepts$", (Integer arg1) -> {
			RemoteWithId remote = (RemoteWithId) world.makeRemote(world.getTheDoor(), arg1);
			remotes.add(remote);

			world.getTheDoor().addRemote(remote);
			assertTrue(world.getTheDoor().getRemotes().size() > 0);
			assertTrue(world.getTheDoor().getRemotes().contains(remote));
		});

		Then("^when I press the button on remote (\\d+) before the door closes automatically$", 
				(Integer remoteId) -> {
					RemoteWithId remote = getRemoteWithId(remoteId);
					assertTrue(remote.getDoor().isOpen());

					remote.press();
					assertTrue(true);
				});

		Then("^after (\\d+) seconds the door remains open$", (Integer delay) -> {
			try {
				Thread.sleep(delay * 1000);
			} catch (InterruptedException e) {
				fail("Sleep interrupted");
			}

			assertTrue(world.getTheDoor().isOpen());		
		});
	}

	private RemoteWithId getRemoteWithId(int id) {
		List<RemoteWithId> result = remotes.stream().filter(r -> r.getId() == id).collect(Collectors.toList());
		if(result.size() > 0)
			return result.get(0);

		throw new IllegalArgumentException("Could not find remote with id: " + id);
	}
}
