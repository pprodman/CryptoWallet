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
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.models.Crypto
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType
import com.example.cryptowallet.viewModel.CryptoViewModel
import java.util.Calendar
import java.util.Date

class CryptoAdapter(
    private val cryptoList: List<Crypto>,
    private val viewModel: CryptoViewModel,
    private val context: Context
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.bind(crypto)
        holder.itemView.setOnClickListener {
            showBuyCryptoDialog(context, crypto.logo, crypto.symbol) { quantity, price, date ->
                // Crear un objeto Date a partir de la fecha en milisegundos
                val transactionDate = Date(date)

                // Crear el objeto Transaction
                val transaction = Transaction(
                    logo = crypto.logo,
                    name = crypto.name,
                    symbol = crypto.symbol,
                    type = TransactionType.BUY,
                    quantity = quantity,
                    pricePerUnit = price,
                    date = transactionDate
                )

                // Manejar la transacción aquí, por ejemplo, guardar en la base de datos usando viewModel
                viewModel.addTransaction(transaction)
            }
        }
    }

    override fun getItemCount(): Int = cryptoList.size

    inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTextView: TextView = itemView.findViewById(R.id.rankTextView)
        private val logoImageView: ImageView = itemView.findViewById(R.id.logoImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val symbolTextView: TextView = itemView.findViewById(R.id.symbolTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

        fun bind(crypto: Crypto) {
            rankTextView.text = "#${crypto.rank}"
            logoImageView.setImageResource(crypto.logo)
            nameTextView.text = crypto.name
            symbolTextView.text = crypto.symbol
            priceTextView.text = "$${crypto.price}"
        }
    }

    private fun showBuyCryptoDialog(
        context: Context,
        logoResId: Int,
        symbol: String,
        onAdd: (quantity: Double, price: Double, date: Long) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_transaction_dialog, null)

        val logoImageView: ImageView = dialogView.findViewById(R.id.logoImageView)
        val symbolTextView: TextView = dialogView.findViewById(R.id.symbolTextView)
        val quantityInput: EditText = dialogView.findViewById(R.id.quantityInput)
        val priceInput: EditText = dialogView.findViewById(R.id.priceInput)
        val dateInput: EditText = dialogView.findViewById(R.id.dateInput)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)
        val addButton: Button = dialogView.findViewById(R.id.addButton)

        logoImageView.setImageResource(logoResId)
        symbolTextView.text = symbol

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

                    onAdd(quantity, price, date)
                    dismiss()
                }
                show()
            }
    }
}