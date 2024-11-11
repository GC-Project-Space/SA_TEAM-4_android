package com.chikorita.gamagochi.presentation.ui.ranking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chikorita.gamagochi.databinding.ItemRankingUserBinding
import com.chikorita.gamagochi.domain.model.MajorInfo

class MajorRankingRVAdapter (private val dataList: ArrayList<MajorInfo>, val mContext : Context): RecyclerView.Adapter<MajorRankingRVAdapter.ViewHolder>(){

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(private val binding: ItemRankingUserBinding):
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: MajorInfo) {
            val rank = adapterPosition + 1 // 인덱스를 1부터 시작하도록 설정
            binding.rank = rank // rank 값을 binding에 설정
            binding.exp = data.totalExperience.toLong()
            binding.symbol = ""
            binding.nickName = data.name
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRankingUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface OnItemClickListener {
        fun onClick(position: Int, rank: Int)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(position, holder.adapterPosition + 1) // 랭킹을 1부터 시작하도록 설정
        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        itemClickListener = onItemClickListener
    }
}
