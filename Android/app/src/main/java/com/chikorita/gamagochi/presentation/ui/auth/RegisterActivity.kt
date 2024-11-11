package com.chikorita.gamagochi.presentation.ui.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.chikorita.gamagochi.R
import com.chikorita.gamagochi.presentation.config.base.BaseActivity
import com.chikorita.gamagochi.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate){
    var inputIsValid = false

    override fun initView() {
        initListener()
    }
    private fun initListener(){
        binding.inputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.countTv.text = count.toString() + "/8"

                inputIsValid = count in 1..8
                // 버튼 활성화 여부 체크
                checkActivateState()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.nextBtn.setOnClickListener{
            if (inputIsValid) {
                val intent = Intent(this, Register2Activity::class.java)
                intent.putExtra("username", binding.inputEt.text.toString())
                startActivity(intent)
                finish()
                overridePendingTransition(0, 0);
            }
        }
        binding.deleteIv.setOnClickListener{
            binding.inputEt.setText("")
        }

    }

    private fun checkActivateState() {
        if (inputIsValid) {
            changeButtonState(true)
        } else changeButtonState(false)
    }

    private fun changeButtonState(status: Boolean) {
        val btn = binding.nextBtn
        val btnColor = if (status) R.color.primary_default else R.color.primary_light

        // 버튼 상태 변경
        btn.isClickable = status
        btn.isEnabled = status
        btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, btnColor))
    }
}