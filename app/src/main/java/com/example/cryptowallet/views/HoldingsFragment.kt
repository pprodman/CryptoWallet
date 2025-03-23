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
import com.example.cryptowallet.adapters.HoldingsAdapter
import com.example.cryptowallet.viewModel.CryptoViewModel

class HoldingsFragment : Fragment() {

    private lateinit var viewModel: CryptoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var holdingsAdapter: HoldingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_holdings, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewHoldings)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[CryptoViewModel::class.java]

        setupRecyclerView()
        observeHoldings()
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
}