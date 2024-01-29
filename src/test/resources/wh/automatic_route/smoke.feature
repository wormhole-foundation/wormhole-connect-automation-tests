Feature: Smoke tests

  Scenario Outline: Wormhole-connect testnet manual smoke
    Given I open wormhole-connect TESTNET and enter password
    And I prepare to send "<amount>" "<asset>" from "<from_network>" using "<from_wallet>" to "<to_network>" using "<to_wallet>" via "<route>"
    When I submit transfer
    Then I should see send from "<scan_from>" link and send to "<scan_to>" link

    Examples:
      | route     | amount | asset | from_network | to_network | from_wallet | to_wallet | scan_from       | scan_to  |
      | manual    | 0.01   | FTM   | Fantom       | Fuji       | MetaMask    | MetaMask  | FtmScan         | Avascan  |
      | manual    | 0.01   | WAVAX | Goerli       | Moonbase   | MetaMask    | MetaMask  | Etherscan       | Moonscan |
      | manual    | 0.3    | CELO  | Alfajores    | Fantom     | MetaMask    | MetaMask  | Celo Explorer   | FtmScan  |
      | automatic | 0.01   | SOL   | Solana       | Fantom     | Phantom     | MetaMask  | Solana Explorer | FtmScan  |
      | automatic | 0.001  | MATIC | Mumbai       | BSC        | MetaMask    | MetaMask  | Polygonscan     | BscScan  |
