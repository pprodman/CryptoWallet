package com.example.cryptowallet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = transactionList.size

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logoImageView: ImageView = itemView.findViewById(R.id.logoImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val symbolTextView: TextView = itemView.findViewById(R.id.symbolTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(transaction: Transaction) {
            logoImageView.setImageResource(transaction.logo)
            nameTextView.text = transaction.name
            symbolTextView.text = transaction.symbol
            typeTextView.text = transaction.type.toString()

            // Cambiar el color según el tipo de transacción
            if (transaction.type == TransactionType.BUY) {
                typeTextView.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark))
            } else if (transaction.type == TransactionType.SELL) {
                typeTextView.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
            }
            // Crear un DecimalFormat para cantidades (4 decimales)
            val quantityFormat = DecimalFormat("#,##0.00##")

            // Crear un DecimalFormat para moneda (2 decimales)
            val currencyFormat = DecimalFormat("#,##0.00")

            quantityTextView.text = "Amount: ${quantityFormat.format(transaction.quantity)}"
            val totalValue = transaction.quantity * transaction.pricePerUnit
            valueTextView.text = "Value: $ ${currencyFormat.format(totalValue)}"
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateTextView.text = dateFormat.format(transaction.date)
        }
    }
}