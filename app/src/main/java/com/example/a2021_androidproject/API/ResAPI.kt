package com.example.a2021_androidproject.API

import com.example.a2021_androidproject.model.ResDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ResAPI {
    @GET("data")
    fun getResName(
        @Query("serviceKey") apiKey :String,
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int
    ): Call<ResDTO>

    @GET("data/search")
    fun searchRes(
        @Query("keyword") keyword : String
    ): Call<ResDTO>
}