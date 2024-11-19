package com.example.weatherappv2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherappv2.database.models.SearchHistoryItem

@Dao
interface SearchHistoryDAO {

    @Insert
    suspend fun insert(query: SearchHistoryItem)

    @Query("SELECT * FROM search_history ORDER BY id DESC")
    suspend fun getAllSearchHistory(): List<SearchHistoryItem>

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()
}