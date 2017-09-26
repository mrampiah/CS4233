Feature: Assignment 1, part 2, Multiple remotes.
  This release, which is the end of the first customer release,
  adds the ability to have multiple remotes for a system. Since
  neighbors might also have Dog Door installed, we need to ensure
  that the only the correct remotes activate the door mechanism.
  New hardware remotes have been created that operate on different
  frequencies as the previous versions. They also send an ID, which
  is an integer, in their press() message.
  
  This version requires a remote ID to be given to it during 
  initialization.
  
  Note: While this may not be the best design it is sufficient for
  a first assignment in this course. We may return to this later to
  examine better ways of designing the system.

  Scenario: My remote opens the door
    Given that I have a dog door that accepts multiple remotes
    And the door is initialized with remote 1
    When I press the button on remote 1
    Then the door is open
    And after 5 seconds the door closes
    Then the door is closed

  Scenario: Someone else's remote does not open the door
  		Given that I have a dog door that accepts multiple remotes
    And the door is initialized with remote 1
    And my neighbor has a remote with ID = 2
    When my neighbor presses the button on remote 2
    Then the door is closed
    
  Scenario: I have two remotes and the second keeps the door open
  		Given that I have a dog door that accepts multiple remotes
    And the door is initialized with remote 1
    And I add remote 42 to the remotes the door accepts
    When I press the button on remote 1
    Then the door is open
    And when I press the button on remote 42 before the door closes automatically
    Then the timer is disabled
    And the door remains open
    
  Scenario: I have three remotes working in conjunction
  		Given that I have a dog door that accepts multiple remotes
    And the door is initialized with remote 1
    And I add remote 42 to the remotes the door accepts
  		And I add remote 17 to the remotes the door accepts
  		When I press the button on remote 1
    Then the door is open
    And when I press the button on remote 42 before the door closes automatically
    Then the timer is disabled
    And after 5 seconds the door remains open
    When I press the button on remote 17
    Then the door is closed
    And the timer is enabled for the next button press