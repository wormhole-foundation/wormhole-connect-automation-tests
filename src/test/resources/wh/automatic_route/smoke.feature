Feature: Smoke tests

  Scenario Outline: Automatic transfer
    Given I open WH main page and enter password
    And I prepare to send "<amount>" "<asset>" from "<from>" using "<from_wallet>" to "<to>" using "<to_wallet>"
    When I submit transfer
    Then I should see send from "<scan_from>" link and send to "<scan_to>" link

    Examples:
      | amount | asset | from      | from_wallet | to       | to_wallet | scan_from       | scan_to  |
      | 0.01   | AVAX  | Fuji      | MetaMask    | Moonbase | MetaMask  | Avascan         | Moonscan |
      | 0.3    | CELO  | Alfajores | MetaMask    | Fantom   | MetaMask  | Celo explorer   | FtmScan  |
      | 0.01   | SOL   | Solana    | Phantom     | Fantom   | MetaMask  | Solana explorer | FtmScan  |
      | 0.001  | MATIC | Mumbai    | MetaMask    | BSC      | MetaMask  | Polygonscan     | BscScan  |
