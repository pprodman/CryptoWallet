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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.models.Holdings
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType
import com.example.cryptowallet.viewModel.CryptoViewModel
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date

/**
 * Adaptador de RecyclerView para mostrar una lista de criptomonedas en la wallet.
 * Permite manejar la selección de una criptomoneda y su venta.
 *
 * @param holdingsList Lista de criptomonedas en la wallet.
 * @param viewModel Modelo de vista para interactuar con la lógica de la aplicación.
 * @param context Contexto de la actividad.
 */
class HoldingsAdapter(
    private val holdingsList: List<Holdings>,
    private val viewModel: CryptoViewModel,
    private val context: Context
) : RecyclerView.Adapter<HoldingsAdapter.HoldingViewHolder>() {

    /**
     * Crea y retorna un nuevo ViewHolder para las criptomonedas en la wallet.
     *
     * @param parent El contenedor de la vista en el que se creará el ViewHolder.
     * @param viewType El tipo de vista para el ViewHolder.
     * @return Un nuevo objeto HoldingViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_holdings, parent, false)
        return HoldingViewHolder(view)
    }

    /**
     * Enlaza los datos de una criptomoneda al ViewHolder y configura las acciones de clic.
     *
     * @param holder El ViewHolder donde se enlazará la criptomoneda.
     * @param position La posición de la criptomoneda en la lista.
     */
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

    /**
     * @return La cantidad de criptomonedas en la lista.
     */
    override fun getItemCount(): Int = holdingsList.size


    /**
     * ViewHolder para representar una criptomoneda en la wallet.
     *
     * @param itemView Vista de la criptomoneda.
     */
    inner class HoldingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val logoImageView: ImageView = itemView.findViewById(R.id.logoImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val averagePriceTextView: TextView = itemView.findViewById(R.id.averageBuyPriceTextView)
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.valueTextView)
        private val profitLossTextView: TextView = itemView.findViewById(R.id.profitLossTextView)

        /**
         * Enlaza los datos de una criptomoneda al ViewHolder.
         *
         * @param holdings Criptomoneda a mostrar.
         */
        fun bind(holdings: Holdings) {
            logoImageView.setImageResource(holdings.logo)
            nameTextView.text = "${holdings.name} (${holdings.symbol})" // Nombre y símbolo juntos

            // Crear un DecimalFormat para cantidades (4 decimales)
            val quantityFormat = DecimalFormat("#,##0.00##")

            // Crear un DecimalFormat para moneda (2 decimales)
            val currencyFormat = DecimalFormat("#,##0.00")

            priceTextView.text = "$ ${currencyFormat.format(viewModel.getCryptoPrice(holdings.symbol))}" // Precio desde Crypto model
            averagePriceTextView.text = "AVG Buy Price: $ ${currencyFormat.format(holdings.averagePrice)}"
            amountTextView.text = "${quantityFormat.format(holdings.totalQuantity)} ${holdings.symbol}"
            valueTextView.text = "$ ${currencyFormat.format(holdings.totalQuantity * viewModel.getCryptoPrice(holdings.symbol))}" // Valor total
            profitLossTextView.text = "$ ${currencyFormat.format(holdings.profitLoss)}"

            // Cambiar el color según pérdidas o ganancias
            if (holdings.profitLoss > 0) {
                profitLossTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else if (holdings.profitLoss < 0) {
                profitLossTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
            }
        }
    }

    /**
     * Función para mostrar el diálogo de venta de una criptomoneda.
     *
     * @param context Contexto de la actividad
     * @param holdings Criptomoneda a vender
     * @param onSell Función a ejecutar al vender la criptomoneda
     */
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
        quantityInput.hint = "Available amount: ${holdings.totalQuantity}" // Usando totalQuantity
        priceInput.hint = "Sell price"
        dateInput.hint = "Sell date"

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
                // Hacer el fondo del diálogo transparente
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                // Configurar botones
                cancelButton.setOnClickListener { dismiss() }
                addButton.setOnClickListener {
                    val quantity = quantityInput.text.toString().toDoubleOrNull() ?: 0.0
                    val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0
                    val date = calendar.timeInMillis

                    if (quantity > holdings.totalQuantity) { // Usando totalQuantity
                        Toast.makeText(context, "Insufficient amount", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    onSell(quantity, price, date)
                    dismiss()
                }
                show()
            }
    }
}