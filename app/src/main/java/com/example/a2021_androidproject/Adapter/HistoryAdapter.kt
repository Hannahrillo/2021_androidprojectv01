package com.example.a2021_androidproject.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2021_androidproject.databinding.ItemHistoryBinding
import com.example.a2021_androidproject.databinding.ItemResBinding
import com.example.a2021_androidproject.model.History
import com.example.a2021_androidproject.model.Restaurant

class HistoryAdapter(val historyDelectClickedListner: (String)-> Unit): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    inner class HistoryItemViewHolder(private val binding : ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(historyModel: History){
            binding.historykeywordTextview.text = historyModel.keyword

            binding.historydeleteBtn.setOnClickListener{
                historyDelectClickedListner(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}