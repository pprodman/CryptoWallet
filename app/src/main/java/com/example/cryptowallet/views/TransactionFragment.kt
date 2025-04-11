package com.example.cryptowallet.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.adapters.TransactionAdapter
import com.example.cryptowallet.viewModel.CryptoViewModel

/**
 * Fragmento que muestra el listado de transacciones.
 * Carga la información en los items del `RecyclerView` a través del `Adapter` y la muestra en la interfaz de usuario.
 *
 * @see viewModel Para gestionar la lógica de la aplicación.
 * @see recyclerView Para mostrar la lista de criptomonedas en el mercado.
 */
class TransactionFragment : Fragment() {

    private lateinit var viewModel: CryptoViewModel
    private lateinit var recyclerView: RecyclerView

    /**
     * Método que crea la vista del fragmento.
     *
     * @param inflater El objeto LayoutInflater para inflar la vista.
     * @param container El contenedor del fragmento.
     * @param savedInstanceState El estado guardado del fragmento, si existe.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewTransaction)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        return view
    }

    /**
     * Método que se llama después de que la vista del fragmento haya sido creada.
     * Se encarga de inicializar el ViewModel, cargar los detalles de criptomonedas y configurar el comportamiento de la interfaz de usuario.
     *
     * @param view La vista raíz del fragmento.
     * @param savedInstanceState El estado guardado del fragmento, si existe.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CryptoViewModel::class.java]
        recyclerView.adapter = TransactionAdapter(viewModel.transactionHistory.value ?: emptyList())

    }
}