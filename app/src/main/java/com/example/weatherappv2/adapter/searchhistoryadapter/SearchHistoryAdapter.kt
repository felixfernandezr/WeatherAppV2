package com.example.weatherappv2.adapter.searchhistoryadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappv2.database.models.SearchHistoryItem
import com.example.weatherappv2.databinding.ItemFragmentHistoryBinding


class SearchHistoryAdapter(private var items: List<SearchHistoryItem>) :
    RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    inner class SearchHistoryViewHolder(val binding: ItemFragmentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val binding = ItemFragmentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val searchHistory = items[position]
        holder.binding.tvLocation.text = "${searchHistory.cityName}, ${searchHistory.cityCountry}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newHistoryList: List<SearchHistoryItem>) {
        items = newHistoryList
        notifyDataSetChanged()
    }
}