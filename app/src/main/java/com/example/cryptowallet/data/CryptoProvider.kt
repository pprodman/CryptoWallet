package com.example.cryptowallet.data

import com.example.cryptowallet.R
import com.example.cryptowallet.models.Crypto

class CryptoProvider {
    companion object {
        val cryptoList = listOf<Crypto>(
            Crypto(1, R.drawable.btc, "Bitcoin", "BTC", 30000.0),
            Crypto(2, R.drawable.eth, "Ethereum", "ETH", 2000.0),
            Crypto(3, R.drawable.xrp, "Ripple", "XRP", 0.5),
            Crypto(4, R.drawable.usdt, "Tether USDt", "USDT", 1.0),
            Crypto(5, R.drawable.sol, "Solana", "SOL", 50.0),
            Crypto(6, R.drawable.bnb, "Binance Coin", "BNB", 200.0),
            Crypto(7, R.drawable.doge, "Dogecoin", "DOGE", 0.05),
            Crypto(8, R.drawable.ada, "Cardano", "ADA", 1.0),
            Crypto(9, R.drawable.usdc, "USD Coin", "USDC", 1.0),
            Crypto(10, R.drawable.trx, "Tron", "TRX", 0.05),
            Crypto(11, R.drawable.avax, "Avalanche", "AVAX", 1.0),
            Crypto(12, R.drawable.shib, "Shiba Inu", "SHIB", 0.000001),
            Crypto(13, R.drawable.ton, "Toncoin", "TON", 0.2),
            Crypto(14, R.drawable.dot, "Polkadot", "DOT", 10.0),
            Crypto(15, R.drawable.link, "Chainlink", "LINK", 20.0),
            Crypto(16, R.drawable.xlm, "Stellar", "XLM", 0.2),
            Crypto(17, R.drawable.sui, "Sui", "SUI", 1.0),
            Crypto(18, R.drawable.bch, "Bitcoin Cash", "BCH", 250.0),
            Crypto(19, R.drawable.hbar, "Hedera", "HBAR", 0.05),
            Crypto(20, R.drawable.ltc, "Litecoin", "LTC", 100.0),
            Crypto(21, R.drawable.near, "NEAR Protocol", "NEAR", 3.0),
            Crypto(22, R.drawable.uni, "Uniswap", "UNI", 1.0),
            Crypto(23, R.drawable.pepe, "Pepe", "PEPE", 0.05),
            Crypto(24, R.drawable.leo, "LEO Token", "LEO", 1.0),
            Crypto(25, R.drawable.apt, "Aptos", "APT", 0.2),
            Crypto(26, R.drawable.icp, "Internet Computer", "ICP", 1.0),
            Crypto(27, R.drawable.cro, "Cronos", "CRO", 0.5),
            Crypto(28, R.drawable.vet, "VeChain", "VET", 0.05),
            Crypto(29, R.drawable.etc, "Ethereum Classic", "ETC", 2.0),
            Crypto(30, R.drawable.dai, "Dai", "DAI", 1.0),
            Crypto(31, R.drawable.tao, "TomoChain", "TAO", 0.1),
            Crypto(32, R.drawable.fet, "Fetch.ai", "FET", 0.05),
            Crypto(33, R.drawable.usde, "Ethena USDe", "USDE", 1.0),
            Crypto(34, R.drawable.fil, "Filecoin", "FIL", 1.0),
            Crypto(35, R.drawable.arb, "Arbitrum", "ARB", 0.05),
            Crypto(36, R.drawable.stx, "Stacks", "STX", 0.1),
            Crypto(37, R.drawable.kas, "Kaspa", "KAS", 0.05),
            Crypto(38, R.drawable.algo, "Algorand", "ALGO", 1.0),
            Crypto(39, R.drawable.atom, "Cosmos", "ATOM", 10.0),
            Crypto(40, R.drawable.aave, "Aave", "AAVE", 1.0),
            Crypto(41, R.drawable.mnt, "Mantle", "MNT", 1.0),
            Crypto(42, R.drawable.tia, "Celestia", "TIA", 0.1),


        )
    }
}