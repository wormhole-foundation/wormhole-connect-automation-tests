Feature: Manual route support to and from Solana on testnet

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
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.001  | MATIC | Mumbai       | Solana     | MetaMask    | Phantom   |
      | wormhole-bridge-manual | 0.001  | CELO  | Alfajores    | Solana     | MetaMask    | Phantom   |

    @smoke
    Examples:
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.001  | AVAX  | Fuji         | Solana     | MetaMask    | Phantom   |
      | wormhole-bridge-manual | 0.01   | SOL   | Solana       | Fantom     | Phantom     | MetaMask  |

    #not dApp warning is shown when Phantom extension is opened
    @ignore
    Examples:
      | route                  | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | wormhole-bridge-manual | 0.001  | MATIC | Mumbai       | Solana     | MetaMask    | Phantom   |
      | wormhole-bridge-manual | 0.001  | AVAX  | Fuji         | Solana     | MetaMask    | Phantom   |
      | wormhole-bridge-manual | 0.001  | CELO  | Alfajores    | Solana     | MetaMask    | Phantom   |
      | wormhole-bridge-manual | 0.01   | SOL   | Solana       | Fuji       | Phantom     | MetaMask  |
      | wormhole-bridge-manual | 0.01   | SOL   | Solana       | Fantom     | Phantom     | MetaMask  |
      | wormhole-bridge-manual | 0.01   | SOL   | Solana       | BSC        | Phantom     | MetaMask  |
