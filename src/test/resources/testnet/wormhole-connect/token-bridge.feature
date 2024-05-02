Feature: Token bridge, wormhole-connect testnet

  Scenario Outline: Automatic route
    Given I launch testnet browser
    Given I open wormhole-connect testnet
    And I enter page password
    And I check native balance on "<to_network>" using "<to_wallet>"
    Given I open wormhole-connect testnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    And I move slider
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain


    Examples:
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | xlabs-bridge-automatic | 0.15   | MATIC | Mumbai       | Fantom     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.15   | MATIC | Mumbai       | Alfajores  | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.15   | MATIC | Mumbai       | Moonbase   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.15   | MATIC | Mumbai       | BSC        | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.6    | MATIC | Mumbai       | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 0.001  | BNB   | BSC          | Mumbai     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.001  | BNB   | BSC          | Alfajores  | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.009  | BNB   | BSC          | Fuji       | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.001  | BNB   | BSC          | Moonbase   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.0025 | BNB   | BSC          | Solana     | MetaMask    | Phantom   |
      | xlabs-bridge-automatic | 0.0002 | BNB   | BSC          | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 0.003  | AVAX  | Fuji         | Mumbai     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.003  | AVAX  | Fuji         | Fantom     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.07   | AVAX  | Fuji         | BSC        | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.003  | AVAX  | Fuji         | Alfajores  | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.003  | AVAX  | Fuji         | Moonbase   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.03   | AVAX  | Fuji         | Solana     | MetaMask    | Phantom   |
      | xlabs-bridge-automatic | 0.03   | AVAX  | Fuji         | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 0.2    | FTM   | Fantom       | Mumbai     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 4      | FTM   | Fantom       | BSC        | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 3      | FTM   | Fantom       | Fuji       | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.2    | FTM   | Fantom       | Alfajores  | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.2    | FTM   | Fantom       | Moonbase   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 2      | FTM   | Fantom       | Solana     | MetaMask    | Phantom   |
      | xlabs-bridge-automatic | 0.6    | FTM   | Fantom       | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 0.16   | CELO  | Alfajores    | Moonbase   | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.16   | CELO  | Alfajores    | Fantom     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.16   | CELO  | Alfajores    | Mumbai     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 1.8    | CELO  | Alfajores    | Fuji       | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 1.2    | CELO  | Alfajores    | Solana     | MetaMask    | Phantom   |
      | xlabs-bridge-automatic | 0.7    | CELO  | Alfajores    | Sui        | MetaMask    | Sui       |
      | xlabs-bridge-automatic | 0.3    | GLMR  | Moonbase     | Mumbai     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.4    | GLMR  | Moonbase     | Fantom     | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 0.4    | GLMR  | Moonbase     | Alfajores  | MetaMask    | MetaMask  |
      | xlabs-bridge-automatic | 1.5    | GLMR  | Moonbase     | Sui        | MetaMask    | Sui       |


  Scenario Outline: Manual route
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
      | route                  | amount | asset  | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.0001 | WMATIC | Mumbai       | BSC        | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | WETH   | Mumbai       | Fuji       | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | USDT   | Mumbai       | Fantom     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | DAI    | Mumbai       | Alfajores  | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | WBNB   | Mumbai       | Moonbase   | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | WAVAX  | Mumbai       | Solana     | MetaMask    | Phantom   |
      | wormhole-bridge-manual | 0.0001 | WFTM   | Mumbai       | Sui        | MetaMask    | Sui       |
      | wormhole-bridge-manual | 0.0001 | CELO   | Mumbai       | Aptos      | MetaMask    | Spika     |
      | wormhole-bridge-manual | 0.0001 | WGLMR  | Mumbai       | Klaytn     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | WSOL   | Fantom       | Sei        | MetaMask    | Leap      |
      | wormhole-bridge-manual | 0.0001 | SUI    | Mumbai       | Klaytn     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | APT    | Mumbai       | Klaytn     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | SEI    | Mumbai       | Klaytn     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | BNB    | BSC          | Alfajores  | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | WGLMR  | BSC          | Moonbase   | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | WMATIC | BSC          | Fuji       | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | AVAX   | Fuji         | Fantom     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | AVAX   | Fuji         | Fantom     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | AVAX   | Fuji         | BSC        | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.1    | KLAY   | Klaytn       | Fantom     | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.1    | KLAY   | Klaytn       | Moonbase   | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.1    | KLAY   | Klaytn       | Fuji       | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.1    | KLAY   | Klaytn       | BSC        | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.1    | GLMR   | Moonbase     | Sepolia    | MetaMask    | MetaMask  |
      | wormhole-bridge-manual | 0.1    | SOL    | Solana       | BSC        | Phantom     | MetaMask  |
      | wormhole-bridge-manual | 0.0001 | SUI    | Sui          | Solana     | Sui         | Phantom   |
