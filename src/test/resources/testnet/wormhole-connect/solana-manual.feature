Feature: Solana  testnet

  Scenario Outline: Solana manual testnet
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


    #To Solana, manual
    Examples:
      | route  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.001  | AVAX  | Fuji         | Solana     | MetaMask    | Phantom   |
      | manual | 0.001  | CELO  | Alfajores    | Solana     | MetaMask    | Phantom   |
      | manual | 0.001  | MATIC | Mumbai       | Solana     | MetaMask    | Phantom   |


#    From Solana
    Examples:
      | route  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.01   | SOL   | Solana       | Fuji       | Phantom     | MetaMask  |
      | manual | 0.01   | SOL   | Solana       | Fantom     | Phantom     | MetaMask  |
      | manual | 0.01   | SOL   | Solana       | BSC        | Phantom     | MetaMask  |

