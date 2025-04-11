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
import com.example.cryptowallet.models.Crypto
import com.example.cryptowallet.models.Transaction
import com.example.cryptowallet.models.TransactionType
import com.example.cryptowallet.viewModel.CryptoViewModel
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date

/**
 * Adaptador de RecyclerView para mostrar una lista de criptomonedas.
 * Permite manejar la selección de una criptomoneda y su compra.
 *
 * @param cryptoList Lista de criptomonedas a mostrar.
 * @param viewModel Modelo de vista para interactuar con la lógica de la aplicación.
 * @param context Contexto de la actividad.
 */
class CryptoAdapter(
    private val cryptoList: List<Crypto>,
    private val viewModel: CryptoViewModel,
    private val context: Context
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    /**
     * Crea y retorna un nuevo ViewHolder para las criptomonedas.
     *
     * @param parent El contenedor de la vista en el que se creará el ViewHolder.
     * @param viewType El tipo de vista para el ViewHolder.
     * @return Un nuevo objeto CrytoViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(view)
    }

    /**
     * Enlaza los datos de una criptomoneda al ViewHolder y configura las acciones de clic.
     *
     * @param holder El ViewHolder donde se enlazará la criptomoneda.
     * @param position La posición de la criptomoneda en la lista.
     */
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.bind(crypto)
        holder.itemView.setOnClickListener {
            showBuyCryptoDialog(context, crypto.logo, crypto.symbol, viewModel.availableBalance.value ?: 0.0) { quantity, price, date ->
                // Calcular el costo total
                val totalCost = quantity * price

                // Verificar si hay suficiente saldo
                if (totalCost > (viewModel.availableBalance.value ?: 0.0)) {
                    Toast.makeText(context, "Insufficient balance", Toast.LENGTH_SHORT).show()
                    return@showBuyCryptoDialog
                }

                // Crear transacción
                val transactionDate = Date(date)
                val transaction = Transaction(
                    logo = crypto.logo,
                    name = crypto.name,
                    symbol = crypto.symbol,
                    type = TransactionType.BUY,
                    quantity = quantity,
                    pricePerUnit = price,
                    date = transactionDate
                )

                // Añadir transacción
                viewModel.addTransaction(transaction)
            }
        }
    }

    /**
     * @return La cantidad de criptomonedas en la lista.
     */
    override fun getItemCount(): Int = cryptoList.size

    /**
     * ViewHolder para representar una criptomoneda en la lista.
     *
     * @param itemView Vista de la criptomoneda.
     */
    inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTextView: TextView = itemView.findViewById(R.id.rankTextView)
        private val logoImageView: ImageView = itemView.findViewById(R.id.logoImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val symbolTextView: TextView = itemView.findViewById(R.id.symbolTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

        /**
         * Enlaza los datos de una criptomoneda al ViewHolder.
         *
         * @param crypto Criptomoneda a mostrar.
         */
        fun bind(crypto: Crypto) {
            rankTextView.text = "#${crypto.rank}"
            logoImageView.setImageResource(crypto.logo)
            nameTextView.text = crypto.name
            symbolTextView.text = crypto.symbol

            // Crear un DecimalFormat para cantidades (4 decimales)
            val quantityFormat = DecimalFormat("#,##0.00##")
            priceTextView.text = "$ ${quantityFormat.format(crypto.price)}"
        }
    }

    /**
     * Función para mostrar el diálogo de compra de una criptomoneda.
     *
     * @param context Contexto de la actividad
     * @param logoResId ID de la imagen del logo
     * @param symbol Símbolo de la criptomoneda
     * @param availableBalance Saldo disponible
     * @param onAdd Función a ejecutar al agregar la transacción
     */
    private fun showBuyCryptoDialog(
        context: Context,
        logoResId: Int,
        symbol: String,
        availableBalance: Double,
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

        val quantityFormat = DecimalFormat("#,##0.00##")

        // Mostrar saldo disponible
        val balanceTextView: TextView = dialogView.findViewById(R.id.balanceTextView)
        balanceTextView.text = "Available Balance: $ ${quantityFormat.format(availableBalance)}"

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
                // Hacer el fondo del diálogo transparente
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                // Configurar botones
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