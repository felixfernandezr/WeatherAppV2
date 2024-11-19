package com.example.weatherappv2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappv2.adapter.searchhistoryadapter.SearchHistoryAdapter
import com.example.weatherappv2.database.AppDataBase
import com.example.weatherappv2.database.SearchHistoryDAO
import com.example.weatherappv2.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchHistoryAdapter
    private lateinit var searchHistoryDao: SearchHistoryDAO
    private lateinit var appDatabase: AppDataBase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize Room database and DAO
        appDatabase = AppDataBase.getDatabase(requireContext())
        searchHistoryDao = appDatabase.searchHistoryDao()

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Setting up adapter and RecyclerView
        adapter = SearchHistoryAdapter(listOf())
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        // Get LiveData for search history and send it to adapter
        lifecycleScope.launch {
            val history = searchHistoryDao.getAllSearchHistory()
            adapter.updateData(history)
        }

        return binding.root // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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