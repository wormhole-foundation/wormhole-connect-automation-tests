Feature: Smoke tests

  Scenario Outline: Smoke tests
    Given I open WH main page and enter password
    And I prepare to send "<amount>" "<asset>" from "<from>" using "<from_wallet>" to "<to>" using "<to_wallet>" via "<route>"
    When I submit transfer
    Then I should see send from "<scan_from>" link and send to "<scan_to>" link

    Examples:
      | amount | asset | from      | from_wallet | to       | to_wallet | route     | scan_from       | scan_to  |
      | 0.01   | AVAX  | Fuji      | MetaMask    | Moonbase | MetaMask  | manual    | Avascan         | Moonscan |
      | 0.3    | CELO  | Alfajores | MetaMask    | Fantom   | MetaMask  | manual    | Celo Explorer   | FtmScan  |
      | 0.01   | SOL   | Solana    | Phantom     | Fantom   | MetaMask  | automatic | Solana explorer | FtmScan  |
      | 0.001  | MATIC | Mumbai    | MetaMask    | BSC      | MetaMask  | automatic | Polygonscan     | BscScan  |
