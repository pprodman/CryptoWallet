package com.example.cryptowallet.models

/**
 * Modelo que representa una criptomoneda.
 *
 * @property rank El rango de la criptomoneda.
 * @property logo El recurso de imagen de la criptomoneda.
 * @property name El nombre de la criptomoneda.
 * @property symbol El símbolo de la criptomoneda.
 * @property price El precio actual de la criptomoneda.
 */
data class Crypto(
    val rank: Int,
    val logo: Int,
    val name: String,
    val symbol: String,
    val price: Double
)