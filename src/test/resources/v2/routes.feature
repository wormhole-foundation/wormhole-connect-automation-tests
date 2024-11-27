Feature: Check that all routes are working correctly

  Scenario Outline: Check that Manual transfer can be resumed
    Given "<build>" opens
    And Transaction details entered: "<amount>" "<source_asset>" "<source_chain>" to "<destination_asset>" "<destination_chain>", from "<source_wallet>" to "<destination_wallet>" route "<route>"
    And Transaction approved in the wallet
    And Link to Wormholescan is displayed
    And Claim assets on destination network if needed

    Examples:
      | route                     | build                                         | source_wallet | destination_wallet | source_chain | destination_chain | amount | source_asset | destination_asset |
      | Token Bridge Manual route | https://wormhole-connect.netlify.app/         | MetaMask      | MetaMask           | Fantom       | Alfajores         | 0.1    | FTM          | WFTM              |
#      | Token Bridge Manual route | https://wormhole-connect-mainnet.netlify.app/ | MetaMask      | MetaMask           | Fantom       | Celo              | 0.001  | FTM          | WFTM              |
