Feature: ETH Bridge route, Portal Bridge mainnet

  Scenario Outline: Automatic route
    Given I launch mainnet browser
    Given I open portal bridge mainnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain

    Examples:
      | route                | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | eth-bridge-automatic | 0.0004 | WETH  | Polygon      | Avalanche  | MetaMask    | MetaMask  |

