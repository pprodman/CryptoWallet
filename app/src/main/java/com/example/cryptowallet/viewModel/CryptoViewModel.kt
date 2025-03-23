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

    private val _cryptoList = CryptoProvider.cryptoList.toMutableList()
    private val _transactionHistory = MutableLiveData<MutableList<Transaction>>(mutableListOf())
    private val _holdingsList = MutableLiveData<MutableList<Holdings>>(mutableListOf())

    val cryptoList: List<Crypto>
        get() = _cryptoList

    val transactionHistory: LiveData<MutableList<Transaction>>
        get() = _transactionHistory

    val holdingsList: LiveData<MutableList<Holdings>>
        get() = _holdingsList

    fun addTransaction(transaction: Transaction) {
        _transactionHistory.value?.add(transaction)
        _transactionHistory.value = _transactionHistory.value // Notificar cambios
        updateHoldings(transaction)
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