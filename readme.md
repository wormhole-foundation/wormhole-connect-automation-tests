# Setup (MacOS)
1. Download Chrome for Testing from here: https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/114.0.5735.90/mac-arm64/chrome-mac-arm64.zip
2. Unzip `chrome-mac-arm64.zip`
3. Remove "downloaded from internet" attribute:
```
cd <path to unzipped chrome-mac-arm64>
sudo xattr -cr 'Google Chrome for Testing.app'
```
4. Copy 'Google Chrome for Testing.app' to Applications
5. Install Xcode from App Store
6. Install IntelliJ IDEA CE
7. Install Rosetta:
```
/usr/sbin/softwareupdate --install-rosetta --agree-to-license
```

# Setup project
```
brew install --cask git-credential-manager
git clone https://github.com/tsadovska/wh-automation-tests.git
```

# Setup (Chrome for Testing)

1. Open project in IntelliJ IDEA
2. Open Maven toolbar -> Click "Reload All Maven Projects" button
3. Right click "src/test/java/support/Browser" -> "Run Browser.main()"
4. Open chrome://extensions/
5. Enable Developer mode
6. Drag and drop `metamask-chrome-10.34.5.crx` from IntelliJ IDEA into Chrome
7. Setup metamask wallet.
8. Enable Test networks in MetaMask
9. Open https://faucet.celo.org/alfajores , login with Github
10. Open https://wormhole-connect.netlify.app/ , send 0.1 CELO from Alfajores to Fantom (this is required to make sure that metamask is properly configured)
11. Switch network to Alfajores
12. Setup Phantom wallet:
- Install from https://chromewebstore.google.com/detail/phantom/bfnaelmomeimhlpmgjnjophhpkkoljpa?hl=en-US
- Enable "Testnet Mode" in Settings > Developer Settings


# Run Tests

1. Install "Cucumber for Java" plugin in IntelliJ IDEA
2. Right click on any ".feature" file -> Run

NOTE: when tests are run for the first time, please confirm new networks in MetaMask

# Faucets

Account address: 0x6FBd5A25aa9a2f40E3b013db720c7b46f22C6116

- https://faucet.celo.org/alfajores (login with Github, gives ~5 CELO, worked on 2023-12-10)
- https://stakely.io/en/faucet/celo-celo (worked on 2023-12-10)
- https://faucet.fantom.network/ 
- https://faucet.solana.com/ (gives 5 SOL, worked on 2024-01-16)

Run faucet.feature to request assets from faucet.celo.org automatically.

# Troubleshooting
- Run Browser, check that there are no pending requests in MetaMask
- Run Browser, check that MetaMask network is Alfajores
