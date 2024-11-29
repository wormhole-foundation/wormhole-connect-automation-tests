Feature: Check that all routes are working correctly

  Scenario Outline: Check that Manual transfer can be resumed
    Given Test data: "<URL>" "<route>" "<amount>" "<sourceToken>" "<sourceChain>" "<sourceWallet>" "<destinationToken>" "<destinationChain>" "<destinationWallet>"
    Given I open the page
    And Transaction details entered
#    And Transaction approved in the wallet
#    And Link to Wormholescan is displayed
#    And Claim assets on destination network if needed
#    And I check "<destination_asset>" balance on "<destination_chain>" using "<destination_wallet>"
#    Then I check that amount is received

    Examples:
      | route                     | URL                                   | sourceWallet | destinationWallet | sourceChain | destinationChain | amount | sourceToken | destinationToken |
      | Token Bridge Manual route | https://wormhole-connect.netlify.app/ | MetaMask      | MetaMask           | Fantom       | Alfajores         | 0.1    | FTM          | WFTM              |
#      | Token Bridge Manual route | https://wormhole-connect-mainnet.netlify.app/ | MetaMask      | MetaMask           | Fantom       | Celo              | 0.001  | FTM          | WFTM              |
#      | Token Bridge Manual route | https://portalbridge.com/ | MetaMask      | MetaMask           | Fantom       | Celo              | 0.001  | FTM          | WFTM              |
