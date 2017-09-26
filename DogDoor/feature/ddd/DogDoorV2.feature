Feature: Assignment 1, part 1, configurable closing delay
  This feature adds the capability for the user to change
  the time that a door stays open before automatically closing.
  
  It also adds the behavior for pressing the button when the door 
  is open.
  
  Note that the assumption here is that the door opens and closes
  using no time. This is a reasonable assumption since the door
  mechanism can be set to not respont until an action finishes.

  Scenario: Default setting closes door after 5 seconds
    Given that I have a dog door system
    When the door is initialized
    And I press the remote button
    Then the door is open
    And after 5 seconds the door closes
    Then the door is closed

  Scenario Outline: Setting default close time to 0 keeps door open
    Given that I have a dog door system
    And the door is initialized
    When I change the default close time to 0
    And I press the remote button
    Then after <time> seconds the door is open

    Examples: 
      | time |
      |    1 |
      |    2 |
      |    6 |

  Scenario Outline: Change the default close time
    Given that I have a dog door system
    And the door is initialized
    When I change the default close time to <time>
    And I press the remote button
    Then the door is open
    And after <time> seconds the door closes
    Then the door is closed

    Examples: 
      | time |
      |    1 |
      |    2 |
      |    6 |

  Scenario: Attempt to change default close time to less than 0
    Given that I have a dog door system
    And the door is initialized
    When I change the default close time to -1
    Then an exception is thrown with the message "Default close time must be >= 0"
    And the default close time is 5 seconds

  Scenario: Press button while open quits timer and keeps door open
    Given that I have a dog door system
    And the door is initialized
    And I change the default close time to 1
    When I press the remote button
    And I press the remote button before the door closes automatically
    Then the timer is disabled
    And the door remains open

  Scenario: Close the door by pressing the button while the timer is disabled.
    Given that I have a dog door system
    And the door is initialized
    And I change the default close time to 1
    And that the door is open
    And I press the remote button
    And that the timer is disabled
    When I press the remote button
    Then the door is closed
    And the timer is enabled for the next button press
