Feature: Token bridge, Portal Bridge mainnet

  Scenario Outline: Automatic Bridge, xLabs
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
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | xlabs-bridge-automatic | 0.004  | BNB   | BSC          | Polygon    | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.7    | MATIC | Polygon      | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 1.2    | CELO  | Celo         | Moonbeam   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.04   | AVAX  | Avalanche    | BSC        | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.7 | SUI   | Sui          | Solana     | Sui         | Phantom   |


  Scenario Outline: Manual Bridge, Wormhole
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
      | route                  | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.0001  | CELO  | Celo         | Fantom     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001  | BNB   | BSC          | Moonbeam   | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.00001 | DAI   | Fantom       | Avalanche  | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001  | SUI   | Moonbeam     | Celo       | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001  | WGLMR | Solana       | Avalanche  | Phantom     | MetaMask  |
