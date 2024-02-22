Feature: Automatic route support between EVM-based chains on testnet

  Scenario Outline:
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
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | automatic | 0.003  | AVAX  | Fuji         | Mumbai     | MetaMask    | MetaMask  |
      | automatic | 0.003  | AVAX  | Fuji         | Fantom     | MetaMask    | MetaMask  |
      | automatic | 0.001  | BNB   | BSC          | Alfajores  | MetaMask    | MetaMask  |
      | automatic | 0.009  | BNB   | BSC          | Fuji       | MetaMask    | MetaMask  |
      | automatic | 0.16   | CELO  | Alfajores    | Moonbase   | MetaMask    | MetaMask  |
      | automatic | 0.3    | CELO  | Moonbase     | Alfajores  | MetaMask    | MetaMask  |

    @smoke
    Examples:
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | automatic | 0.001  | BNB   | BSC          | Alfajores  | MetaMask    | MetaMask  |


    @ignore
    Examples:
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | automatic | 0.15   | MATIC | Mumbai       | Alfajores  | MetaMask    | MetaMask  |
      | automatic | 0.15   | MATIC | Mumbai       | Moonbase   | MetaMask    | MetaMask  |
      | automatic | 0.005  | ETH   | Goerli       | Fuji       | MetaMask    | MetaMask  |
      | automatic | 0.0014 | ETH   | Goerli       | BSC        | MetaMask    | MetaMask  |
