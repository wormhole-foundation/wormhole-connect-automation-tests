Feature: Check that all routes are working correctly

  Scenario Outline: Check that Manual transfer can be resumed
    Given "<build>" opens
    And Transaction details entered: "<amount>" "<source_asset>" "<source_chain>" to "<destination_asset>" "<destination_chain>", route "<route>"
    And Transaction approved in the wallet
    And Link to Wormholescan is displayed
    Then Balance has increased on destination chain


    Examples:
      | source_chain | destination_chain | amount | source_asset | destination_asset | route                     | build                                         |
      | Fantom       | Celo              | 0.001  | FTM          | FTWM              | Token Bridge Manual route | https://wormhole-connect-mainnet.netlify.app/ |
