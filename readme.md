# Setup (MacOS)
1. Download Chrome for Testing from here: https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/114.0.5735.90/mac-arm64/chrome-mac-arm64.zip
2. Unzip `chrome-mac-arm64.zip`
3. Remove "downloaded from internet" attribute:
```
cd <path to unzipped chrome-mac-arm64>
sudo xattr -cr 'Google Chrome for Testing.app'
```
4. Copy 'Google Chrome for Testing.app' to Applications
5. Install IntelliJ IDEA CE
6. Install Rosetta:
```
/usr/sbin/softwareupdate --install-rosetta --agree-to-license
```
7. Make sure "Require password after screen saver begins or displayed is turned off" setting is set to 1 hour or more

# Setup project
```
brew install --cask git-credential-manager
git clone https://github.com/tsadovska/wormhole-connect-automation-tests
```

# Setup (Chrome for Testing)

1. Metamask extension
   * Open project in IntelliJ IDEA 
   * Open Maven toolbar -> Click "Reload All Maven Projects" button 
   * Right click "src/test/java/support/Browser" -> "Run Browser.main()"
   * Open chrome://extensions/ 
   * Enable Developer mode 
   * Drag and drop `metamask-chrome-10.34.5.crx` from IntelliJ IDEA into Chrome
   * Enable Test networks in MetaMask
   * Put wallet unlock password to `.env` file
2. Setup Phantom wallet:
- Install from https://chromewebstore.google.com/detail/phantom/bfnaelmomeimhlpmgjnjophhpkkoljpa?hl=en-US
- Enable "Testnet Mode" in Settings > Developer Settings
- Put wallet unlock password to `.env` file
3. Setup Leap wallet:
- Install from https://chromewebstore.google.com/detail/leap-cosmos-wallet/fcfcfllfndlomdhbehjjcoimbgofdncg?hl=en
- Change "Network" to "Testnet"
- Put wallet unlock password to `.env` file
4. Setup Sui wallet:
- Install from https://chromewebstore.google.com/detail/sui-wallet/opcgpfmipidbgpenhmajoajpbobppdil
- Change "Network" to "Testnet"
- Put wallet unlock password to `.env` file

For mainnet testing, do the same steps with BrowserMainnet.
To start mainnet browser: right click "src/test/java/support/BrowserMainnet" -> "Run BrowserMainnet.main()".

# Run Tests

1. Install "Cucumber for Java" plugin in IntelliJ IDEA
2. Right click on any ".feature" file -> Run
