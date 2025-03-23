package com.example.cryptowallet.models

data class Holdings(
    val logo: Int,
    val name: String,
    val symbol: String,
    var totalQuantity: Double = 0.0,
    var averagePrice: Double = 0.0,
    var profitLoss: Double = 0.0,
)
