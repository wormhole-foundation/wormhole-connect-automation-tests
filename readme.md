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
6. Install Rosetta (only applies to M1+ processors):
```
/usr/sbin/softwareupdate --install-rosetta --agree-to-license
```
7. Make sure "Require password after screen saver begins or displayed is turned off" setting is set to 1 hour or more

# Setup project
```
brew install --cask git-credential-manager
git clone https://github.com/tsadovska/wormhole-connect-automation-tests
```
Copy `.env.example` to `.env` file and enter missing values. 

# Setup Google Chrome for Testing

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

# Build MetaMask extension
```
git clone https://github.com/MetaMask/metamask-extension.git
git checkout master
```
Follow MetaMask [Building on your local machine](https://github.com/MetaMask/metamask-extension#building-on-your-local-machine) guide:
```
brew install corepack
corepack enable
cp .metamaskrc{.dist,}
# create an account on https://app.infura.io/register
# make sure all networks are enabled for the new Infura API Key
# Update .metmaskrc file: replace the INFURA_PROJECT_ID value with your own personal Infura API Key. 
yarn install
node ./development/build/index.js dist --apply-lavamoat=false
```
Open `chrome://extensions/`, click "Pack extension"
Set `Extension root directory` to "dist/chrome" directory inside metamask-extension directory.
Click `Pack extension`.

Generated "chrome.crx" can now be installed in Google Chrome for Testing.

# Setup Google Spreadsheets

1. Create a Google Spreadsheet, update `.env` file.
2. Follow [this guide](https://developers.google.com/sheets/api/quickstart/java) to create `credentials.json` file.
3. Save `credentials.json` file to `src/test/resources` directory.
4. Follow [this guide](https://developers.google.com/drive/api/quickstart/java) to enable Google Drive API.
