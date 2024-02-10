Feature: Cosmos mainnet

  Scenario Outline: Cosmos
    Given I open wormhole-connect mainnet and enter password
    And I prepare to send "<amount>" "<asset>" from "<from_network>" using "<from_wallet>" to "<to_network>" using "<to_wallet>" via "<route>"


    Examples:
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | automatic | 0.001  | CELO  | Kujira       | Evmos      | Leap        | Leap      |
