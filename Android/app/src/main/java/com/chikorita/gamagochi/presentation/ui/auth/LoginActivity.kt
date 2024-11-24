package com.chikorita.gamagochi.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chikorita.gamagochi.R
import com.chikorita.gamagochi.data.UserDatabase
import com.chikorita.gamagochi.presentation.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.login_btn).setOnClickListener {
            val loginSuccessful = performLogin() // 로그인 로직 구현 필요
            if (loginSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun performLogin(): Boolean {
        // 로그인 로직 구현
        return false // 임시 반환값
    }
}