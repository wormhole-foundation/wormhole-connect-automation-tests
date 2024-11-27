Feature: Cosmos route, Portal Bridge mainnet

  Scenario Outline: Automatic
    Given I launch mainnet browser
    Given I open portal bridge mainnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain

    Examples:
      | route            | amount | asset  | from_network | to_network | from_wallet | to_wallet |
      | cosmos-automatic | 0.0001 | CELO   | Kujira       | Osmosis    | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | WMATIC | Kujira       | Injective  | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | WFTM   | Evmos        | Osmosis    | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | SUI    | Evmos        | Kujira     | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | APT    | Evmos        | Injective  | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | CELO   | Osmosis      | Kujira     | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | PYTH   | Osmosis      | Injective  | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | BONK   | Injective    | Osmosis    | Leap        | Leap      |
      | cosmos-automatic | 0.0001 | WSOL   | Injective    | Kujira     | Leap        | Leap      |


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
      | cosmos-manual | 0.00001 | USDT  | Osmosis      | BSC        | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | WBNB  | Osmosis      | Polygon    | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | SUI   | Osmosis      | Avalanche  | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | APT   | Evmos        | Fantom     | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | CELO  | Evmos        | Celo       | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | WFTM  | Evmos        | Moonbeam   | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | APT   | Evmos        | Solana     | Leap        | Phantom   |
      | cosmos-manual | 0.00001 | APT   | Kujira       | Sui        | Leap        | Sui       |
      | cosmos-manual | 0.00001 | APT   | Kujira       | Aptos      | Leap        | Spika     |
      | cosmos-manual | 0.00001 | APT   | Kujira       | Base       | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | DAI   | Injective    | Arbitrum   | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | WAVAX | Injective    | Optimism   | Leap        | MetaMask  |
      | cosmos-manual | 0.00001 | WGLMR | Injective    | Klaytn     | Leap        | MetaMask  |
