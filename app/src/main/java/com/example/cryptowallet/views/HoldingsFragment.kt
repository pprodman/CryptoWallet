package com.example.cryptowallet.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.adapters.HoldingsAdapter
import com.example.cryptowallet.viewModel.CryptoViewModel
import java.text.DecimalFormat

class HoldingsFragment : Fragment() {

    private lateinit var viewModel: CryptoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var holdingsAdapter: HoldingsAdapter
    private lateinit var depositTextView: TextView

    // Crear un DecimalFormat para moneda (2 decimales)
    private val currencyFormat = DecimalFormat("#,##0.00")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_holdings, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewHoldings)
        depositTextView = view.findViewById(R.id.depositTextView)

        // Configurar el botón para añadir saldo
        val addBalanceButton = view.findViewById<Button>(R.id.addBalanceButton)
        addBalanceButton.setOnClickListener {
            showAddBalanceDialog()
        }

        // Configurar el botón para retirar saldo
        val withdrawBalanceButton = view.findViewById<Button>(R.id.withdrawBalanceButton)
        withdrawBalanceButton.setOnClickListener {
            showWithdrawBalanceDialog()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CryptoViewModel::class.java]

        setupRecyclerView()
        observeHoldings()

        // Observar cambios en el saldo
        viewModel.availableBalance.observe(viewLifecycleOwner) { balance ->
            depositTextView.text = "Balance: $ ${currencyFormat.format(balance)}"
        }

        // Observar cambios en el valor total de los holdings
        viewModel.totalHoldingsValue.observe(viewLifecycleOwner) { totalValue ->
            val totalHoldingsValueTextView = view.findViewById<TextView>(R.id.totalHoldingsValueTextView)
            totalHoldingsValueTextView.text = "$ ${currencyFormat.format(totalValue)}"
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeHoldings() {
        viewModel.holdingsList.observe(viewLifecycleOwner) { holdingsList ->
            holdingsAdapter = HoldingsAdapter(holdingsList, requireContext(), viewModel)
            recyclerView.adapter = holdingsAdapter
        }
    }

    // Implementación de la función showAddBalanceDialog
    private fun showAddBalanceDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.add_saldo_dialog, null)

        val amountInput = dialogView.findViewById<EditText>(R.id.amountInput)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val addButton = dialogView.findViewById<Button>(R.id.addButton)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
            .apply {
                cancelButton.setOnClickListener { dismiss() }
                addButton.setOnClickListener {
                    val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
                    if (amount > 0) {
                        viewModel.addBalance(amount)
                        Toast.makeText(requireContext(),
                            "Balance added: $ ${currencyFormat.format(amount)}",
                            Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(),
                            "Please enter a valid amount",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                show()
            }
    }

    // Implementación de la función para mostrar el diálogo de retirar saldo
    private fun showWithdrawBalanceDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.withdraw_saldo_dialog, null)

        val currentBalanceText = dialogView.findViewById<TextView>(R.id.currentBalanceText)
        val amountInput = dialogView.findViewById<EditText>(R.id.amountInput)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val withdrawButton = dialogView.findViewById<Button>(R.id.withdrawButton)

        // Mostrar el saldo actual
        val currentBalance = viewModel.availableBalance.value ?: 0.0
        currentBalanceText.text = "Current Balance: $ ${currencyFormat.format(currentBalance)}"

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
            .apply {
                cancelButton.setOnClickListener { dismiss() }
                withdrawButton.setOnClickListener {
                    val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0

                    if (amount <= 0) {
                        Toast.makeText(requireContext(),
                            "Please enter a valid amount",
                            Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (amount > currentBalance) {
                        Toast.makeText(requireContext(),
                            "Insufficient balance",
                            Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (viewModel.withdrawBalance(amount)) {
                        Toast.makeText(requireContext(),
                            "Withdrew: $ ${currencyFormat.format(amount)}",
                            Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        // Este caso no debería ocurrir debido a las validaciones anteriores
                        Toast.makeText(requireContext(),
                            "Failed to withdraw. Please try again.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                show()
            }
    }

}