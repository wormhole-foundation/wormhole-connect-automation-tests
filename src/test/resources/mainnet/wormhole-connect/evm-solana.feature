Feature: EVM manual testnet

  Scenario Outline: EVM manual testnet
    Given I open wormhole-connect mainnet and enter password
    And I prepare to send "<amount>" "<asset>" from "<from_network>" using "<from_wallet>" to "<to_network>" using "<to_wallet>" via "<route>"
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should claim assets
    Then I should see Send To link
    Then I check balance has increased on destination chain

    Examples:
      | route  | amount  | asset | from_network | to_network | from_wallet | to_wallet |
      | manual | 0.001   | CELO  | Celo         | Solana     | MetaMask    | Phantom   |
      | manual | 0.00001 | BNB   | BSC          | Solana     | MetaMask    | Phantom   |
      | manual | 0.00001 | SOL   | Solana       | Avalanche  | Phantom     | MetaMask  |
