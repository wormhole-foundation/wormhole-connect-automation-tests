Feature: Check that all routes are working correctly

  Scenario Outline: Check that Manual transfer can be resumed
    Given "<build>" opens
    And Transaction details entered: <amount> <source asset> <source chain> to <destination asset> <destination chain>, route <route>
    And Transaction approved in the wallet
    And Link to Wormholescan is displayed
    Then Balance has increased on destination chain


    Examples:
      | source_chain | destination_chain | amount | source_asset | destination_asset | route| build|
