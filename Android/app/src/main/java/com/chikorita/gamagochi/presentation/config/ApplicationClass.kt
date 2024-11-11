package com.chikorita.gamagochi.presentation.config

import android.app.Application
import android.content.SharedPreferences
import com.chikorita.gamagochi.BuildConfig
import com.chikorita.gamagochi.presentation.di.NetworkModule
import com.chikorita.gamagochi.presentation.utils.DataStoreManager
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass: Application() {
    @Inject
    @NetworkModule.BasicRetrofit
    lateinit var basicRetrofit: Retrofit

    // 서버 주소
    val API_URL = BuildConfig.BASE_URL

    init {
        instance = this
    }

    // 코틀린의 전역변수 문법
    companion object {
        lateinit var instance: ApplicationClass
            private set

        var missions: List<Long> = listOf(1L,2L,3L)
        lateinit var dsManager: DataStoreManager

        // 만들어져있는 SharedPreferences를 사용
        val sSharedPreferences: SharedPreferences
            get() = instance.getSharedPreferences("NAMO", MODE_PRIVATE)

        // JWT Token Header 키 값
        const val X_ACCESS_TOKEN = "X_ACCESS_TOKEN"
        const val X_REFRESH_TOKEN = "X_REFRESH_TOKEN"
    }

    // 앱이 처음 생성되는 순간, SP를 새로 만들어주고, 레트로핏 인스턴스를 생성합니다.
    override fun onCreate() {
        super.onCreate()

        dsManager = DataStoreManager(applicationContext)

        // SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}