Feature: Circle CCTP, manual

  Scenario Outline:
    Given I launch testnet browser
    Given I open wormhole-connect testnet
    And I enter page password
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should claim assets
    Then I should see Send To link
    Then I check balance has increased on destination chain


    Examples:
      | route     | amount | asset | from_network | to_network      | from_wallet | to_wallet |
      | automatic | 0.0001 | USDC  | Goerli       | Optimism        | MetaMask    | MetaMask  |
      | automatic | 0.0001 | USDC  | Mumbai       | Arbitrum Goerli | MetaMask    | MetaMask  |