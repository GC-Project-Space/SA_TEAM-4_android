package com.chikorita.gamagochi.presentation.ui.ranking

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chikorita.gamagochi.R
import com.chikorita.gamagochi.databinding.FragmentRankingBinding
import com.chikorita.gamagochi.presentation.config.base.BaseBindingFragment
import com.chikorita.gamagochi.presentation.ui.MainViewModel
import com.chikorita.gamagochi.presentation.ui.ranking.adapter.MajorRankingRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MajorRankingFragment(fragmentId: Int): BaseBindingFragment<FragmentRankingBinding>(R.layout.fragment_ranking) {

    val viewModel: MainViewModel by viewModels()
    val fragmentId = fragmentId
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val logoImageView = binding.mudangLogo
        val rotateAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_logo_360)
        logoImageView.startAnimation(rotateAnimation)

        CoroutineScope(Dispatchers.IO).launch {
            setDataView()
            viewModel.getAllMajor()

        }
    }

    private suspend fun setDataView() {
        withContext(Dispatchers.Main) {
        with(viewModel){
                majorList.observe(viewLifecycleOwner) {


                    val majorAdapter =
                        activity?.let { it1 -> MajorRankingRVAdapter(it, it1) }
                    binding.exampleRecyclerView.apply {
                        adapter = majorAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                    majorAdapter?.setOnItemClickListener(object :
                        MajorRankingRVAdapter.OnItemClickListener {

                        override fun onClick(position: Int, rank: Int) {
                        }
                    })

                } }
        }
    }
}