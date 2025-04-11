package com.example.cryptowallet.models

import java.util.Date

/**
 * Modelo que representa una transacción de una criptomoneda.
 *
 * @property logo El recurso de imagen de la criptomoneda.
 * @property name El nombre de la criptomoneda.
 * @property symbol El símbolo de la criptomoneda.
 * @property type El tipo de transacción (compra o venta).
 * @property quantity La cantidad de la criptomoneda en la transacción.
 * @property pricePerUnit El precio por unidad de la criptomoneda.
 * @property date La fecha de la transacción.
 */
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