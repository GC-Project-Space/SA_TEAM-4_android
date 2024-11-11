package com.chikorita.gamagochi.presentation.ui.auth

import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.chikorita.gamagochi.R
import com.chikorita.gamagochi.data.dto.GetMajorListResult
import com.chikorita.gamagochi.data.dto.SetUserInfoResponse
import com.chikorita.gamagochi.databinding.ActivityRegister2Binding
import com.chikorita.gamagochi.domain.repository.RankRepository.Companion.USER_ID
import com.chikorita.gamagochi.presentation.config.ApplicationClass.Companion.sSharedPreferences
import com.chikorita.gamagochi.presentation.config.base.BaseActivity
import com.chikorita.gamagochi.presentation.ui.OnBoardingActivity

enum class Major (val ko: String) {
    SOFTWARE("소프트웨어학과"),
    ARTIFICIAL_INTELLIGENCE("인공지능학과"),
    COMPUTER_ENGINEERING("컴퓨터공학과"),
    ELECTRONIC_ENGINEERING("전기공학과"),
    INDUSTRY_ENGINEERING("산업공학과"),
    PHYSICS("물리학과"),
    BIOLOGY_ENGINEERING("생명공학과"),
    CHEMISTRY("화학과"),
    PSYCHOLOGY("심리학과"),
    MATHEMATICS("수학과"),
    HISTORY("역사학과"),
    CHEMICAL_ENGINEERING("화학공학과"),
    SOCIOLOGY("사회학과"),
    LAW("법학과"),
    MEDICAL_ENGINEERING("의료공학과"),
    EDUCATION("교육학과");
}


class Register2Activity : BaseActivity<ActivityRegister2Binding>(ActivityRegister2Binding::inflate), AdapterView.OnItemSelectedListener{
    var inputIsValid = true
    var items : Array<String> = arrayOf()
    var eItems : Array<String> = arrayOf()
    lateinit var spinner : Spinner
    lateinit var username : String

    override fun initView() {
        response()
        initListener()
        spinnerAdapter()

        username = intent.getStringExtra("username").toString()
    }
    private fun initListener(){

        val userId = sSharedPreferences.getInt(USER_ID.toString(), 1351345461)
        binding.nextBtn.setOnClickListener{

//            RegisterService(this).trySetUserInfo(userId, username, toEn(spinner.selectedItem.toString()))

            if(inputIsValid) {
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun spinnerAdapter(){
        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner = binding.inputEt
        spinner.setAdapter(spinnerAdapter)

        spinner.setOnItemSelectedListener(this)
    }

    private fun response(){
        items = arrayOf(
            "소프트웨어학과",
            "인공지능학과",
            "컴퓨터공학과",
            "전기공학과",
            "산업공학과",
            "물리학과",
            "생명공학과",
            "화학과",
            "심리학과",
            "수학과",
            "역사학과",
            "화학공학과",
            "사회학과",
            "법학과",
            "의료공학과",
            "교육학과"
        )
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        //spinner.text(items[p2]);
        changeButtonState(true)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //spinner.setText(items[0]);
        changeButtonState(false)
    }

    private fun changeButtonState(status: Boolean) {
        val btn = binding.nextBtn
        val btnColor = if (status) R.color.primary_default else R.color.primary_light

        // 버튼 상태 변경
        btn.isClickable = status
        btn.isEnabled = status
        btn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, btnColor))
    }

    private fun toEn(text: String) : String {
        for (b in Major.values()) {
            if (b.ko == text) {
                return b.name;
            }
        }
        return "?";
    }
}