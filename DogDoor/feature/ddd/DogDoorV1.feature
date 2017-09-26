Feature: Dog door version 1
  Background
  This set of scenarios describes the behavior expcected for the first release
  of Doug's Dog Door system. The overall system description comes from 
  "Head First Object-Oriented Analysis and Design," chapter 2.
  
  Scenario: On initialization the door is closed
  		When the door is initialized
  		Then the door is closed

  Scenario: Open door
    Given that the door is closed
    When I press the remote button
    Then the door is open
    And after 5 seconds the door closes
    Then the door is closed

	## This scenario is superceded by a new scenario for version 2.
  #Scenario: Close door
    #Given that the door is open
    #When I press the remote button
    #Then the door is closed
