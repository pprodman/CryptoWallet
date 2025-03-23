package com.example.cryptowallet.adapters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.models.Holdings
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType
import com.example.cryptowallet.viewModel.CryptoViewModel
import java.util.Calendar
import java.util.Date

class HoldingsAdapter(
    private val holdingsList: List<Holdings>,
    private val context: Context,
    private val viewModel: CryptoViewModel
) : RecyclerView.Adapter<HoldingsAdapter.HoldingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_holdings, parent, false)
        return HoldingViewHolder(view)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        val holdings = holdingsList[position]
        holder.bind(holdings)
        holder.itemView.setOnClickListener {
            showSellCryptoDialog(context, holdings) { quantity, price, date ->
                val transactionDate = Date(date)
                val transaction = Transaction(
                    logo = holdings.logo,
                    name = holdings.name,
                    symbol = holdings.symbol,
                    type = TransactionType.SELL,
                    quantity = quantity,
                    pricePerUnit = price,
                    date = transactionDate
                )
                viewModel.addTransaction(transaction)
            }
        }
    }

    override fun getItemCount(): Int = holdingsList.size

    inner class HoldingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logoImageView: ImageView = itemView.findViewById(R.id.logoImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val averagePriceTextView: TextView = itemView.findViewById(R.id.averageBuyPriceTextView)
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
        private val profitLossTextView: TextView = itemView.findViewById(R.id.profitLossTextView)

        fun bind(holdings: Holdings) {
            logoImageView.setImageResource(holdings.logo)
            nameTextView.text = "${holdings.name} (${holdings.symbol})" // Nombre y símbolo juntos
            priceTextView.text = "$${viewModel.getCryptoPrice(holdings.symbol)}" // Precio desde Crypto model
            averagePriceTextView.text = "Avg buy price: $${holdings.averagePrice}"
            amountTextView.text = "${holdings.totalQuantity}"
            valueTextView.text = "$${holdings.totalQuantity * viewModel.getCryptoPrice(holdings.symbol)}" // Valor total
            profitLossTextView.text = "$${holdings.profitLoss}"
        }
    }

    private fun showSellCryptoDialog(
        context: Context,
        holdings: Holdings,
        onSell: (quantity: Double, price: Double, date: Long) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_transaction_dialog, null)

        val logoImageView: ImageView = dialogView.findViewById(R.id.logoImageView)
        val symbolTextView: TextView = dialogView.findViewById(R.id.symbolTextView)
        val quantityInput: EditText = dialogView.findViewById(R.id.quantityInput)
        val priceInput: EditText = dialogView.findViewById(R.id.priceInput)
        val dateInput: EditText = dialogView.findViewById(R.id.dateInput)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)
        val addButton: Button = dialogView.findViewById(R.id.addButton)

        logoImageView.setImageResource(holdings.logo)
        symbolTextView.text = holdings.symbol
        quantityInput.hint = "Cantidad disponible: ${holdings.totalQuantity}" // Usando totalQuantity
        priceInput.hint = "Precio de venta"

        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dateInput.setText("$dayOfMonth/${month + 1}/$year")
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        dateInput.setOnClickListener {
            datePickerDialog.show()
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
            .apply {
                cancelButton.setOnClickListener { dismiss() }
                addButton.setOnClickListener {
                    val quantity = quantityInput.text.toString().toDoubleOrNull() ?: 0.0
                    val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0
                    val date = calendar.timeInMillis

                    if (quantity > holdings.totalQuantity) { // Usando totalQuantity
                        Toast.makeText(context, "Cantidad insuficiente", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    onSell(quantity, price, date)
                    dismiss()
                }
                show()
            }
    }
}