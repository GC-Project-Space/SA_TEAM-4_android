package com.chikorita.gamagochi.presentation.config.interceptor

import android.util.Log
import com.chikorita.gamagochi.data.api.RefreshApiService
import com.chikorita.gamagochi.data.dto.RefreshRequest
import com.chikorita.gamagochi.presentation.config.ApplicationClass.Companion.dsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class BaseInterceptor @Inject constructor(
    private val apiService: RefreshApiService
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken: String? = runBlocking { dsManager.getAccessToken().first() }
        val refreshToken: String? = runBlocking { dsManager.getRefreshToken().first() }

        val newRequest = request.newBuilder()

        if (accessToken != null) {
            newRequest.addHeader("Authorization", "Bearer $accessToken") // 요청 헤더에 토큰 붙이기
        }

        val response = chain.proceed(newRequest.build())

        when (response.code) {
            400 -> {
                // Show Bad Request Error Message
            }
            401 -> {
                // Show Forbidden Message
            }
            403 -> {
                // Show Forbidden Message
                Log.d("Token", "403 액세스 토큰 만료")
                // 이전 토큰
                if (accessToken != null) {
                    Log.d("AccessToken", accessToken)
                    Log.d("RefreshToken", "$refreshToken")
                }

                // 재발급 API 호출
                // 받은 토큰 다시 넣어서 기존 api 재호출
                val newTokenResponse = runBlocking { apiService.refreshToken(RefreshRequest(refreshToken.toString())) }

                Log.d("Token", newTokenResponse.toString())

                runBlocking {
                    dsManager.saveAccessToken(newTokenResponse.accessToken)
                    dsManager.saveRefreshToken(newTokenResponse.refreshToken)
                }
                Log.d("onTokenResponse", "토큰 재발급 성공!")

                val newJwtToken = newTokenResponse.accessToken

                // 새로운 토큰으로 하던 작업 서버 재요청
                val finalRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $newJwtToken")
                    .build()

                response.close()
                return chain.proceed(finalRequest)
            }
            404 -> {
                // Show NotFound Message
            }
            // ... and so on
        }

        return response
    }
}