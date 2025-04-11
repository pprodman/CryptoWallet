package com.example.cryptowallet.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.R
import com.example.cryptowallet.adapters.CryptoAdapter
import com.example.cryptowallet.viewModel.CryptoViewModel
import android.widget.SearchView
import com.example.cryptowallet.models.Crypto

/**
 * Fragmento que muestra el listado de criptomonedas en el mercado.
 * Carga la información en los items del `RecyclerView` a través del `Adapter` y la muestra en la interfaz de usuario.
 * Permite al usuario comprar criptomonedas.
 *
 * @see viewModel Para gestionar la lógica de la aplicación.
 * @see recyclerView Para mostrar la lista de criptomonedas en el mercado.
 * @see searchView Para permitir la búsqueda de criptomonedas.
 * @see originalCryptoList Para almacenar la lista original de criptomonedas.
 */
class MarketFragment : Fragment() {

    private lateinit var viewModel: CryptoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView // Declare SearchView
    private lateinit var adapter: CryptoAdapter
    private var originalCryptoList: List<Crypto> = emptyList() // Store original list

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
        val view = inflater.inflate(R.layout.fragment_market, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewMarket)
        searchView = view.findViewById(R.id.searchView) // Initialize SearchView
        recyclerView.layoutManager = LinearLayoutManager(context)
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

        originalCryptoList = viewModel.cryptoList // Initialize original list
        adapter = CryptoAdapter(originalCryptoList, viewModel, requireContext()) // Initialize adapter
        recyclerView.adapter = adapter

        setupSearchView() // Call setupSearchView
    }

    /**
     * Configura el `SearchView` para permitir la búsqueda de criptomonedas.
     */
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCryptoList(newText)
                return true
            }
        })
    }

    /**
     * Filtra la lista de criptomonedas según el texto de búsqueda.
     *
     * @param query El texto de búsqueda.
     */
    private fun filterCryptoList(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            originalCryptoList // Show original list if query is empty
        } else {
            originalCryptoList.filter { crypto ->
                crypto.name.contains(query, ignoreCase = true) ||
                        crypto.symbol.contains(query, ignoreCase = true)
            }
        }
        adapter = CryptoAdapter(filteredList, viewModel, requireContext())
        recyclerView.adapter = adapter
    }
}