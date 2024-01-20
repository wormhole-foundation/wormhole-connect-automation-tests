Feature: Smoke tests

  Scenario Outline: Automatic transfer
    Given I open WH main page and enter password
    And I prepare to send "<amount>" "<asset>" from "<from>" using "<from_wallet>" to "<to>" using "<to_wallet>"
    When I submit transfer
    Then I should see "<scan>" link

    Examples:
      | amount | asset | from      | from_wallet | to       | to_wallet | scan     |
      | 0.01   | AVAX  | Fuji      | MetaMask    | Moonbase | MetaMask  | Moonscan |
      | 0.3    | CELO  | Alfajores | MetaMask    | Fantom   | MetaMask  | FtmScan  |
      | 0.01   | SOL   | Solana    | Phantom     | Fantom   | MetaMask  | FtmScan  |
