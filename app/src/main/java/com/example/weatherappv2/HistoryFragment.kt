package com.example.weatherappv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappv2.adapter.searchhistoryadapter.SearchHistoryAdapter
import com.example.weatherappv2.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchHistoryAdapter
    private var searchList: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieving search list from arguments
        searchList = arguments?.getStringArrayList("searchList")

        // Set up RecyclerView
        adapter = SearchHistoryAdapter(searchList ?: emptyList())
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        // Handling back button click
        binding.backBtn.setOnClickListener {
            (activity as? MainActivity)?.showMainLayout() // // Making MainActivity UI visible
            parentFragmentManager.popBackStack() // Popping fragment from backstack
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}