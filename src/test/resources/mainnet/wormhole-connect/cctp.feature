Feature: CCTP route, wormhole-connect mainnet

  Scenario Outline: Automatic
    Given I launch mainnet browser
    Given I open wormhole-connect mainnet
    And I enter page password
    And I check native balance on "<to_network>" using "<to_wallet>"
    And I open wormhole-connect mainnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    And I move slider
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain

    Examples:
      | route            | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | circle-automatic | 1.5      | USDC  | Base         | Polygon    | MetaMask    | MetaMask  |



  Scenario Outline: Manual
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

    Examples:
      | route         | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | circle-manual | 0.00001 | USDC  | BSC          | Polygon    | MetaMask    | MetaMask  |