Feature: EVM manual testnet

  Scenario Outline: EVM manual testnet
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
      | route  | amount | asset | from_network    | to_network      | from_wallet | to_wallet |
#      | manual | 0.001  | MATIC | Mumbai          | BSC             | MetaMask    | MetaMask  |
#      | manual | 0.001  | BNB   | BSC             | Fuji            | MetaMask    | MetaMask  |
#      | manual | 0.001  | AVAX  | Fuji            | Fantom          | MetaMask    | MetaMask  |
#      | manual | 0.001  | FTM   | Fantom          | Alfajores       | MetaMask    | MetaMask  |
#      | manual | 0.001  | CELO  | Alfajores       | Moonbase        | MetaMask    | MetaMask  |
      | manual | 0.001  | GLMR  | Moonbase        | Arbitrum Goerli | MetaMask    | MetaMask  |
#      | manual | 0.001  | WSOL  | Arbitrum Goerli | Optimism Goerli | MetaMask    | MetaMask  |
#      | manual | 0.001  | ETH   | Optimism Goerli | Goerli          | MetaMask    | MetaMask  |
#      | manual | 0.001  | DAI   | Goerli          | Mumbai          | MetaMask    | MetaMask  |
