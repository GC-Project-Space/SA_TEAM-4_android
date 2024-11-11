package com.chikorita.gamagochi.presentation.ui

import android.content.Intent
import com.chikorita.gamagochi.presentation.config.base.BaseActivity
import com.chikorita.gamagochi.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(
    ActivityOnBoardingBinding::inflate){
    override fun initView() {
        initListener()
    }
    private fun initListener(){

        binding.nextBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}