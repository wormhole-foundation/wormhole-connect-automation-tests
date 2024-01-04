Feature: Smoke tests

  Scenario Outline: Automatic transfer
    Given I open WH main page and enter password
    And I prepare to send "<amount>" "<asset>" from "<from>" to "<to>"
    When I submit transfer
    Then I should see "<scan>" link

    Examples:
      | amount  | asset  | from      | to          | scan     |
      |   0.01  | AVAX   | Fuji      | Moonbase    | Moonscan |
      |   0.3   | CELO   | Alfajores | Fantom      | FtmScan  |

