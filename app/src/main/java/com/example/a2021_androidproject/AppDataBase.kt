package com.example.a2021_androidproject

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a2021_androidproject.Dao.HistoryDao
import com.example.a2021_androidproject.Dao.ReviewDao
import com.example.a2021_androidproject.model.History
import com.example.a2021_androidproject.model.Review

@Database(entities = [History::class, Review::class], version=1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
    abstract fun reviewDao() : ReviewDao
}