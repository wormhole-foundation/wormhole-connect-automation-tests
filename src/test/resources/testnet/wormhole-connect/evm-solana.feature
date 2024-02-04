Feature: EVM manual testnet

  Scenario Outline: EVM manual testnet
    Given I open wormhole-connect TESTNET and enter password
    And I prepare to send "<amount>" "<asset>" from "<from_network>" using "<from_wallet>" to "<to_network>" using "<to_wallet>" via "<route>"
    When I submit transfer
    Then I should see send from "<scan_from>" link
    Then I should claim assets
    Then I should see send to "<scan_to>" link

    Examples:
      | route  | amount | asset | from_network    | to_network      | from_wallet | to_wallet | scan_from                | scan_to                  |
      | manual | 0.001  | MATIC | Mumbai          | Solana             | MetaMask    | MetaMask  | Polygonscan              | Solana explorer                  |



