Feature: EVM manual testnet

  Scenario Outline: EVM manual testnet
    Given I open wormhole-connect TESTNET and enter password
    And I prepare to send "<amount>" "<asset>" from "<from_network>" using "<from_wallet>" to "<to_network>" using "<to_wallet>" via "<route>"
    When I submit transfer
    Then I should see send from "<scan_from>" link
    Then I should claim assets
    Then I should see send to "<scan_to>" link
    Then I check final balance

    Examples:
      | route  | amount | asset | from_network    | to_network      | from_wallet | to_wallet | scan_from                | scan_to                  |
      | manual | 0.001  | MATIC | Mumbai          | BSC             | MetaMask    | MetaMask  | Polygonscan              | BscScan                  |
      | manual | 0.001  | BNB   | BSC             | Fuji            | MetaMask    | MetaMask  | BscScan                  | Avascan                  |
      | manual | 0.001  | AVAX  | Fuji            | Fantom          | MetaMask    | MetaMask  | Avascan                  | FtmScan                  |
      | manual | 0.001  | FTM   | Fantom          | Alfajores       | MetaMask    | MetaMask  | FtmScan                  | Celo Explorer            |
      | manual | 0.001  | CELO  | Alfajores       | Moonbase        | MetaMask    | MetaMask  | Celo Explorer            | Moonscan                 |
      | manual | 0.001  | GLMR  | Moonbase        | Base Goerli     | MetaMask    | MetaMask  | Moonscan                 | BaseScan                 |
#      | manual | 0.001  | USDC  | Base Goerli     | Arbitrum Goerli | MetaMask    | MetaMask  | BaseScan                 | Arbitrum Goerli Explorer |
#      | manual | 0.001  | WSOL  | Arbitrum Goerli | Optimism Goerli | MetaMask    | MetaMask  | Arbitrum Goerli Explorer | Optimism Goerli          |
#      | manual | 0.001  | CELO  | Optimism Goerli | Goerli          | MetaMask    | MetaMask  | Celo Explorer            | FtmScan                  |
#      | manual | 0.001  | ETH   | Goerli          | Mumbai          | MetaMask    | MetaMask  | Etherscan                | Polygonscan              |


