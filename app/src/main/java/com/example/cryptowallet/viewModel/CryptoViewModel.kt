package com.example.cryptowallet.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptowallet.data.CryptoProvider
import com.example.cryptowallet.models.Crypto
import com.example.cryptowallet.models.Holdings
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType

class CryptoViewModel : ViewModel() {

    private val _availableBalance = MutableLiveData<Double>(0.0) // Saldo disponible
    private val _totalHoldingsValue = MutableLiveData<Double>(0.0) // Valor total de las holdings
    private val _cryptoList = CryptoProvider.cryptoList// Lista de criptomonedas
    private val _transactionHistory = MutableLiveData<MutableList<Transaction>>(mutableListOf()) // Historial de transacciones
    private val _holdingsList = MutableLiveData<MutableList<Holdings>>(mutableListOf()) // Lista de holdings

    val availableBalance: LiveData<Double>
        get() = _availableBalance

    val totalHoldingsValue: LiveData<Double>
        get() = _totalHoldingsValue

    val cryptoList: List<Crypto>
        get() = _cryptoList

    val transactionHistory: LiveData<MutableList<Transaction>>
        get() = _transactionHistory

    val holdingsList: LiveData<MutableList<Holdings>>
        get() = _holdingsList


    // Agregar saldo
    fun addBalance(amount: Double) {
        _availableBalance.value = (_availableBalance.value ?: 0.0) + amount
    }

    // Método para retirar saldo
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

    // Agregar transacción
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

        _transactionHistory.value?.add(transaction)
        _transactionHistory.value = _transactionHistory.value // Notificar cambios
        updateHoldings(transaction)
        return true
    }

    // Calcular el valor total de las holdings
    private fun calculateTotalHoldingsValue() {
        val totalValue = _holdingsList.value?.sumOf { holding ->
            holding.totalQuantity * getCryptoPrice(holding.symbol)
        } ?: 0.0
        _totalHoldingsValue.value = totalValue
    }

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

    private fun calculateHoldings() {
        _holdingsList.value?.forEach { holding ->
            val currentPrice = getCryptoPrice(holding.symbol)
            holding.profitLoss = (currentPrice - holding.averagePrice) * holding.totalQuantity
        }
        _holdingsList.value = _holdingsList.value // Notificar cambios
    }

    fun getCryptoPrice(symbol: String): Double {
        return _cryptoList.find { it.symbol == symbol }?.price ?: 0.0
    }
}