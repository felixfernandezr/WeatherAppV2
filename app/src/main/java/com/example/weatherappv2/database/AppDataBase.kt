package com.example.weatherappv2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherappv2.database.models.SearchHistoryItem

@Database(entities = [SearchHistoryItem::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}