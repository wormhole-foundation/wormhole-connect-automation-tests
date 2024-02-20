Feature: CCTP automatic route support on testnet

  Scenario Outline:
    Given I launch testnet browser
    Given I open wormhole-connect testnet
    And I enter page password
    And I check native balance on "<to_network>" using "<to_wallet>"
    Given I open wormhole-connect testnet
    And I prepare to send "<amount>" "<asset>" from "<from_network>"("<from_wallet>") to "<to_network>"("<to_wallet>") with "<route>" route
    And I move slider
    When I click on Approve button
    When I approve wallet notifications
    Then I should see Send From link
    Then I should see Send To link
    Then I check balance has increased on destination chain


    # Transactions between Goerli, Mumbai, Fuji, Base Goerli, Arbitrum Goerli, Optimism Goerli

    @new
    Examples:
      | route            | amount | asset | from_network | to_network | from_wallet | to_wallet |
      | circle-automatic | 1.1    | USDC  | Mumbai       | Fuji       | MetaMask    | MetaMask  |
      | circle-automatic | 0.15   | USDC  | Fuji         | Mumbai     | MetaMask    | MetaMask  |


    @ignore
    Examples:
      | route            | amount | asset | from_network    | to_network      | from_wallet | to_wallet |
      | circle-automatic | 1.1    | USDC  | Goerli          | Base Goerli     | MetaMask    | MetaMask  |
      | circle-automatic | 1.1    | USDC  | Base Goerli     | Optimism Goerli | MetaMask    | MetaMask  |
      | circle-automatic | 1.1    | USDC  | Arbitrum Goerli | Goerli          | MetaMask    | MetaMask  |
      | circle-automatic | 1.1    | USDC  | Optimism Goerli | Arbitrum Goerli | MetaMask    | MetaMask  |

