Feature: Token bridge, wormhole-connect mainnet

  Scenario Outline: Automatic Bridge, xLabs
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
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | xlabs-bridge-automatic | 0.002  | BNB   | BSC          | Polygon    | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.5    | MATIC | Polygon      | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 0.8    | CELO  | Celo         | Moonbeam   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.04   | AVAX  | Avalanche    | BSC        | MetaMask    | MetaMask  |


  Scenario Outline: Manual Bridge, Wormhole
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
      | route                  | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.0001  | CELO  | Celo         | Fantom     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001  | BNB   | BSC          | Moonbeam   | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.00001 | DAI   | Fantom       | Avalanche  | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001  | SUI   | Moonbeam     | Celo       | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001  | WGLMR | Solana       | Avalanche  | Phantom     | MetaMask  |
