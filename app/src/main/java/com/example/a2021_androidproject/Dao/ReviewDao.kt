package com.example.a2021_androidproject.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.a2021_androidproject.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE id == :id")
    fun getOnReview(id:Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review:Review)
}