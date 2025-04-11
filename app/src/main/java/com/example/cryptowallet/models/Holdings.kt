package com.example.cryptowallet.models

/**
 * Modelo que representa una criptomoneda como activo en cartera
 *
 * @property logo El recurso de imagen de la criptomoneda.
 * @property name El nombre de la criptomoneda.
 * @property symbol El símbolo de la criptomoneda.
 * @property totalQuantity La cantidad total de la criptomoneda en el cartera.
 * @property averagePrice El precio promedio de compra de la criptomoneda.
 * @property profitLoss La pérdida o ganancia acumulada de la criptomoneda.
 */
data class Holdings(
    val logo: Int,
    val name: String,
    val symbol: String,
    var totalQuantity: Double = 0.0,
    var averagePrice: Double = 0.0,
    var profitLoss: Double = 0.0,
)
