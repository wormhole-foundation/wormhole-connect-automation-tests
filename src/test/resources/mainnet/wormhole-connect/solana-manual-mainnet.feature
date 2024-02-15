Feature: Solana mainnet

  Scenario Outline: Solana mainnet
    Given I launch mainnet browser
    Given I open wormhole-connect mainnet
    And I enter page password
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should claim assets
    Then I should see Send To link
    Then I check balance has increased on destination chain

#    manual route to Solana
    Examples:
      | route  | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.00001 | SOL   | Solana       | Avalanche  | Phantom     | MetaMask  |
      | manual | 0.00001 | SOL   | Solana       | Fantom     | Phantom     | MetaMask  |


#    manual route to Solana
    Examples:
      | route  | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.00001 | BNB   | BSC          | Solana     | MetaMask    | Phantom   |
      | manual | 0.00001 | SOL   | Solana       | Solana     | MetaMask    | Phantom   |
