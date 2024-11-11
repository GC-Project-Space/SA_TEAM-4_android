package com.chikorita.gamagochi.presentation.ui.ranking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chikorita.gamagochi.presentation.ui.ranking.MajorRankingFragment
import com.chikorita.gamagochi.presentation.ui.ranking.SchoolRankingFragment

class RankingVPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SchoolRankingFragment(0)
            else -> MajorRankingFragment(1)
        }
    }
}