package com.example.cryptowallet.models

import java.util.Date

data class Transaction(
    val logo: Int,
    val name: String,
    val symbol: String,
    val type: TransactionType,
    val quantity: Double,
    val pricePerUnit: Double,
    val date: Date,
)

enum class TransactionType {
    BUY, SELL
}