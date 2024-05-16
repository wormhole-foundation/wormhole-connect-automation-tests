Feature: Smoke testnet

  Scenario Outline: Automatic route
    Given I launch testnet browser
    Given I open "https://wormhole-connect.netlify.app/" URL
    And I enter page password
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain


    Examples:
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | xlabs-bridge-automatic | 2      | FTM   | Fantom       | Fuji       | MetaMask    | MetaMask  |


  Scenario Outline: Manual route
    Given I launch testnet browser
    Given I open "https://wormhole-connect.netlify.app/" URL
    And I enter page password
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should claim assets
    Then I should see Send To link
    Then I check balance has increased on destination chain


    Examples:
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.0001 | CELO  | Alfajores    | Moonbase   | MetaMask    | MetaMask  |
