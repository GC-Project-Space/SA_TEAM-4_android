package com.chikorita.gamagochi.presentation.ui.ranking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chikorita.gamagochi.data.dto.RankingList
import com.chikorita.gamagochi.databinding.ItemRankingUserBinding

class SchoolRankingRVAdapter(private val dataList: ArrayList<RankingList>, val mContext : Context): RecyclerView.Adapter<SchoolRankingRVAdapter.ViewHolder>(){

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(private val binding: ItemRankingUserBinding):
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: RankingList) {
            binding.rank = data.rank
            binding.exp = data.experience.toLong()
            binding.symbol = data.ladybugType
            binding.nickName = data.nickName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRankingUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(position)
        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }
}
