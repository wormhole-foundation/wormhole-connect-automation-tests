Feature: CCTP route, Portal Bridge mainnet

  Scenario Outline: Automatic
    Given I launch mainnet browser
    Given I open portal bridge mainnet
    And I check native balance on "<to_network>" using "<to_wallet>"
    And I open portal bridge mainnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    And I move slider
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain

    Examples:
      | route            | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | circle-automatic | 1.5    | USDC  | Ethereum     | Arbitrum   | MetaMask    | MetaMask  |
      | circle-automatic | 1.5    | USDC  | Polygon      | Avalanche  | MetaMask    | MetaMask  |
      | circle-automatic | 1.5    | USDC  | Optimism     | Arbitrum   | MetaMask    | MetaMask  |

  Scenario Outline: Manual
    Given I launch mainnet browser
    Given I open portal bridge mainnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should claim assets
    Then I should see Send To link
    Then I check balance has increased on destination chain

    Examples:
      | route         | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | circle-manual | 0.01    | USDC  | Avalanche    | Base       | MetaMask    | MetaMask  |
      | circle-manual | 0.01    | USDC  | Base         | Polygon    | MetaMask    | MetaMask  |
