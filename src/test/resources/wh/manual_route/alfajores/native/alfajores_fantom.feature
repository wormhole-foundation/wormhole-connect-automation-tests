Feature: Alfajores
  Transfer tokens from Alfajores

  Scenario: Transfer native token from Alfajores to Fantom
    Given I open WH main page and enter password
    And I prepare to send "0.01" "CELO" from "Alfajores" to "Fantom"
    When I submit transfer
    Then I claim token
    Then I should see FtmScan link
