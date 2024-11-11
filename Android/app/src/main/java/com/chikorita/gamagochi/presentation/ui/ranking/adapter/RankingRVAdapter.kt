package com.chikorita.gamagochi.presentation.ui.ranking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chikorita.gamagochi.databinding.ItemRankingUserBinding
import com.chikorita.gamagochi.domain.model.SchoolRanker
import com.chikorita.gamagochi.presentation.ui.MainViewModel

class RankingRVAdapter(private val viewModel: MainViewModel) : RecyclerView.Adapter<RankingRVAdapter.ExampleViewHolder>() {
    lateinit var exampleList: List<SchoolRanker>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        return ExampleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.bind(viewModel, exampleList[position])
    }

    override fun getItemCount() = exampleList.size

    class ExampleViewHolder private constructor(val binding: ItemRankingUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MainViewModel, item: SchoolRanker) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ExampleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRankingUserBinding.inflate(layoutInflater, parent, false)

                return ExampleViewHolder(binding)
            }
        }
    }
}