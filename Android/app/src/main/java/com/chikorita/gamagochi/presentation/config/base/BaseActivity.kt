package com.chikorita.gamagochi.presentation.config.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.chikorita.gamagochi.presentation.utils.KeyboardVisibilityUtils
import dagger.hilt.android.AndroidEntryPoint

// 액티비티의 기본을 작성, 뷰 바인딩 활용
abstract class BaseActivity<B : ViewBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {
    protected lateinit var binding: B
        private set
    lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    // 뷰 바인딩 객체를 받아서 inflate해서 화면을 만들어줌.
    // 즉 매번 onCreate에서 setContentView를 하지 않아도 됨.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)

        binding.root.isFocusableInTouchMode = true
        binding.root.isClickable = true
        binding.root.setOnTouchListener { view, motionEvent ->
            hideKeyboard(view)
            false
        }

        keyboardVisibilityUtils = KeyboardVisibilityUtils(this.window,
            onShowKeyboard = { keyboardHeight ->
            },
            onHideKeyboard = {
            }
        )
        setContentView(binding.root)

        initView()
//        val window = window
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    abstract fun initView()



    override fun onDestroy() {
        keyboardVisibilityUtils.detachKeyboardListeners()
        super.onDestroy()
    }

    // 토스트를 쉽게 띄울 수 있게 해줌.
    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            this.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}