Feature:

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


    #From Goerli
    Examples:
      | route  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.0001 | ETH   | Goerli       | Mumbai     | MetaMask    | MetaMask  |
      | manual | 0.0001 | WETH  | Goerli       | BSC        | MetaMask    | MetaMask  |
      | manual | 0.0001 | DAI   | Goerli       | Fuji       | MetaMask    | MetaMask  |
      | manual | 0.0001 | ETH   | Goerli       | Fantom     | MetaMask    | MetaMask  |
