package com.example.cryptowallet.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptowallet.data.CryptoProvider
import com.example.cryptowallet.models.Crypto
import com.example.cryptowallet.models.Holdings
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType

/**
 * ViewModel para manejar la lógica relacionada con las criptomonedas y sus detalles.
 */
class CryptoViewModel : ViewModel() {

    private val _availableBalance = MutableLiveData<Double>(0.0) // Saldo disponible
    private val _totalHoldingsValue = MutableLiveData<Double>(0.0) // Valor total de las holdings
    private val _totalProfitLoss = MutableLiveData<Double>(0.0) // Pérdida o ganancia total
    private val _cryptoList = CryptoProvider.cryptoList// Lista de criptomonedas
    private val _transactionHistory = MutableLiveData<MutableList<Transaction>>(mutableListOf()) // Historial de transacciones
    private val _holdingsList = MutableLiveData<MutableList<Holdings>>(mutableListOf()) // Lista de holdings

    val availableBalance: LiveData<Double>
        get() = _availableBalance

    val totalHoldingsValue: LiveData<Double>
        get() = _totalHoldingsValue

    val totalProfitLoss: LiveData<Double>
        get() = _totalProfitLoss

    val cryptoList: List<Crypto>
        get() = _cryptoList

    val transactionHistory: LiveData<MutableList<Transaction>>
        get() = _transactionHistory

    val holdingsList: LiveData<MutableList<Holdings>>
        get() = _holdingsList


    /**
     * Agrega saldo al saldo disponible.
     *
     * @param amount La cantidad de saldo a agregar.
     */
    fun addBalance(amount: Double) {
        _availableBalance.value = (_availableBalance.value ?: 0.0) + amount
    }

    /**
     * Retira saldo del saldo disponible.
     *
     * @param amount La cantidad de saldo a retirar.
     */
    fun withdrawBalance(amount: Double): Boolean {
        val currentBalance = _availableBalance.value ?: 0.0
        if (amount <= 0) {
            return false // Cantidad inválida
        }
        if (currentBalance < amount) {
            return false // Saldo insuficiente
        }

        _availableBalance.value = currentBalance - amount
        return true
    }

    /**
     * Agrega una transacción de compra o venta al historial de transacciones.
     *
     * @param transaction La transacción a agregar.
     * @return `true` si la transacción fue exitosa, `false` si falló (ej. saldo insuficiente).
     */
    fun addTransaction(transaction: Transaction): Boolean {
        if (transaction.type == TransactionType.BUY) {
            val totalCost = transaction.quantity * transaction.pricePerUnit
            if (!withdrawBalance(totalCost)) {
                return false // No hay suficiente saldo
            }
        } else { // SELL
            val totalRevenue = transaction.quantity * transaction.pricePerUnit
            addBalance(totalRevenue)
        }

        // Actualizar el historial de transacciones
        val currentHistory = _transactionHistory.value ?: mutableListOf()
        currentHistory.add(transaction)
        currentHistory.sortByDescending { it.date } // Ordenar por fecha de transacción
        _transactionHistory.value = currentHistory // Actualizar el valor del LiveData

        updateHoldings(transaction)
        return true
    }

    /**
     * Calcula el valor total de los criptomonedas en la wallet.
     */
    private fun calculateTotalHoldingsValue() {
        val totalValue = _holdingsList.value?.sumOf { holding ->
            holding.totalQuantity * getCryptoPrice(holding.symbol)
        } ?: 0.0
        _totalHoldingsValue.value = totalValue
    }

    /**
     * Actualiza la lista de criptomonedas en cartera según la transacción (compra o venta) realizada.
     *
     * @param transaction La transacción que se está procesando.
     */
    private fun updateHoldings(transaction: Transaction) {
        val holdingsMap = _holdingsList.value?.associateBy { it.symbol }?.toMutableMap() ?: mutableMapOf()

        if (holdingsMap.containsKey(transaction.symbol)) {
            val holding = holdingsMap[transaction.symbol]!!
            if (transaction.type == TransactionType.BUY) {
                val totalValue = holding.averagePrice * holding.totalQuantity + transaction.pricePerUnit * transaction.quantity
                val newQuantity = holding.totalQuantity + transaction.quantity
                holdingsMap[transaction.symbol] = holding.copy(
                    totalQuantity = newQuantity,
                    averagePrice = totalValue / newQuantity
                )
            } else {
                val newQuantity = holding.totalQuantity - transaction.quantity
                if (newQuantity > 0) {
                    holdingsMap[transaction.symbol] = holding.copy(
                        totalQuantity = newQuantity
                    )
                } else {
                    holdingsMap.remove(transaction.symbol)
                }
            }
        } else if (transaction.type == TransactionType.BUY) {
            holdingsMap[transaction.symbol] = Holdings(
                transaction.logo,
                transaction.name,
                transaction.symbol,
                transaction.quantity,
                transaction.pricePerUnit,
                0.0
            )
        }

        // Filtrar holdings con cantidad mayor a cero
        _holdingsList.value = holdingsMap.values.filter { it.totalQuantity > 0 }.toMutableList()
        calculateHoldings()
        calculateTotalHoldingsValue() // Calcular el valor total de los holdings
    }

    /**
     * Calcula la pérdida o ganancia de cada criptomoneda en cartera.
     */
    private fun calculateHoldings() {
        var totalProfitLoss = 0.0 // Pérdida o ganancia total

        _holdingsList.value?.forEach { holding ->
            val currentPrice = getCryptoPrice(holding.symbol)
            holding.profitLoss = (currentPrice - holding.averagePrice) * holding.totalQuantity
            totalProfitLoss += holding.profitLoss // Actualizar la pérdida o ganancia total
        }
        // Actualizar la pérdida o ganancia total
        _totalProfitLoss.value = totalProfitLoss

        // Actualizar la lista de holdings
        _holdingsList.value = _holdingsList.value
    }

    /**
     * Obtiene el precio actual de una criptomoneda por su símbolo.
     *
     * @param symbol El símbolo de la criptomoneda.
     */
    fun getCryptoPrice(symbol: String): Double {
        return _cryptoList.find { it.symbol == symbol }?.price ?: 0.0
    }
}