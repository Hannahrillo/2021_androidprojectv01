package com.example.a2021_androidproject

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.a2021_androidproject.API.ResAPI
import com.example.a2021_androidproject.Adapter.HistoryAdapter
import com.example.a2021_androidproject.databinding.ActivityMainBinding
import com.example.a2021_androidproject.model.History
import com.example.a2021_androidproject.model.ResDTO
import com.example.a2021_androidproject.model.Restaurant
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    val restList:List<Restaurant> = mutableListOf()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ResAdapter
    private lateinit var historyAdaper : HistoryAdapter
    private lateinit var ResService : ResAPI

    private lateinit var db :AppDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initResRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "ResSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://androidguzo.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ResService = retrofit.create(ResAPI::class.java)
        val jsonObject = JSONObject()


        ResService.getResName(
            getString(R.string.resAPIKey),
            10,
            1
        )
            .enqueue(object: Callback<ResDTO> {
                //성공.
                override fun onResponse(call: Call<ResDTO>, response: Response<ResDTO>) {
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT Sucess")
                        return
                    }
                    Log.e(TAG, "Sucess")
                    Log.e(TAG, response.toString())

                    response.body()?.let {
                        it.restaurants.forEach{ restaurant ->
                            restList.toMutableList().add(restaurant)
                        }
                        adapter.submitList(it.restaurants)

                    }

                }

                //실패처리.
                override fun onFailure(call: Call<ResDTO>, t: Throwable) {
                    Log.e(TAG, t.toString() )
                    Log.e(TAG, "실패" )
                }

            })
        //initSearchEditText()

    }


    private fun search(keyword :String){
        ResService.searchRes(keyword)
            .enqueue( object : Callback<ResDTO> {
                //성공.
                override fun onResponse(call: Call<ResDTO>, response: Response<ResDTO>) {
                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT Sucess")
                        return
                    }
                    adapter.submitList(response.body()?.restaurants.orEmpty())
                    Log.d(TAG,"sucess asdf")
                }

                override fun onFailure(call: Call<ResDTO>, t: Throwable) {
                    hideHistoryView()
                }
            })
    }


    private fun initResRecyclerView(){
        adapter = ResAdapter(itemClickedListner = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("ResModel", it)
            startActivity(intent)
        })
        binding.resRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.resRecyclerview.adapter = adapter
    }

    private fun initHistoryRecyclerView(){
        historyAdaper = HistoryAdapter(historyDelectClickedListner  ={
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerview.layoutManager=LinearLayoutManager(this)
        binding.historyRecyclerview.adapter=historyAdaper
        initSearchEditText()
    }

    private fun initSearchEditText(){
        binding.searchEditText.setOnKeyListener{v,keyCode, event->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                Log.e(TAG,"검색버튼 누름")
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }

            return@setOnKeyListener false

        }
        binding.searchEditText.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryView(){
        Thread{
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread{
                binding.historyRecyclerview.isVisible = true
                historyAdaper.submitList(keywords.orEmpty())
            }

        }.start()
        binding.historyRecyclerview.isVisible =true
    }

    private fun hideHistoryView(){
        binding.historyRecyclerview.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String){
        Thread{
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String){
        Thread{
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }



    companion object{
        private const val TAG = "MainActivity"
    }
}