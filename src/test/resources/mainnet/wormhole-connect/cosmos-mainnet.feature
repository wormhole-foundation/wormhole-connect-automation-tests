Feature: Cosmos mainnet

  Scenario Outline: Cosmos mainnet
    Given I launch mainnet browser
    Given I open wormhole-connect mainnet
    And I enter page password
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route


    Examples:
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | automatic | 0.001  | CELO  | Kujira       | Evmos      | Leap        | Leap      |
