Feature: EVM manual testnet

  Scenario Outline: EVM manual testnet
    Given I open wormhole-connect TESTNET and enter password
    And I prepare to send "<amount>" "<asset>" from "<from_network>" using "<from_wallet>" to "<to_network>" using "<to_wallet>" via "<route>"
    And I move slider
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should claim assets
    Then I should see Send To link
    Then I check balance on destination chain

    Examples:
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | automatic | 0.2    | MATIC | Mumbai       | Alfajores  | MetaMask    | MetaMask  |
      | automatic | 0.008  | BNB   | BSC          | Goerli     | MetaMask    | MetaMask  |
