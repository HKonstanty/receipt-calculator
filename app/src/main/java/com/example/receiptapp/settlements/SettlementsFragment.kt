package com.example.receiptapp.settlements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.settlementsData.Settlement


class SettlementsFragment : Fragment() {

    private val settlementsListViewModel by viewModels<SettlementsListViewModel> {
        SettlementsListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settlements, container, false)
        val settlementRecyclerView = view.findViewById<RecyclerView>(R.id.settlements_recycler_view)
        val settlementAdapter = SettlementAdapter()
        settlementRecyclerView.adapter = settlementAdapter

        settlementsListViewModel.settlementsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                settlementAdapter.submitList(it as MutableList<Settlement>)
            }
        }
        return view
    }
}