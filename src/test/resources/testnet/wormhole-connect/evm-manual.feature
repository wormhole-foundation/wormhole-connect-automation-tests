Feature: Manual route support between EVM-based chains on testnet

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
      | route  | amount | asset  | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.0001 | BNB    | BSC          | Alfajores  | MetaMask    | MetaMask  |
      | manual | 0.0001 | WGLMR  | BSC          | Moonbase   | MetaMask    | MetaMask  |
      | manual | 0.0001 | WMATIC | BSC          | Fuji       | MetaMask    | MetaMask  |
      | manual | 0.0001 | AVAX   | Fuji         | Fantom     | MetaMask    | MetaMask  |
      | manual | 0.0001 | AVAX   | Fuji         | Goerli     | MetaMask    | MetaMask  |
      | manual | 0.1    | KLAY   | Klaytn       | Fantom     | MetaMask    | MetaMask  |
      | manual | 0.1    | KLAY   | Klaytn       | Moonbase   | MetaMask    | MetaMask  |

    @smoke
    Examples:
      | route  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.1    | KLAY  | Klaytn       | Fantom     | MetaMask    | MetaMask  |
      | manual | 0.0001 | BNB   | BSC          | Alfajores  | MetaMask    | MetaMask  |

    @ignore
    Examples:
      | route  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.0001 | MATIC | Mumbai       | Goerli     | MetaMask    | MetaMask  |
      | manual | 0.0001 | WAVAX | Mumbai       | BSC        | MetaMask    | MetaMask  |
      | manual | 0.0001 | CELO  | Mumbai       | Fuji       | MetaMask    | MetaMask  |
      | manual | 0.0001 | WFTM  | Mumbai       | Fantom     | MetaMask    | MetaMask  |


