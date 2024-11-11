package com.chikorita.gamagochi.domain.repository

import android.util.Log
import com.chikorita.gamagochi.data.api.AuthApiService
import com.chikorita.gamagochi.data.dto.KakaoSDKResponse
import com.chikorita.gamagochi.data.dto.KakaoSDKResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    /** 카카오 로그인 */
    suspend fun postKakaoLogin(accessToken: String): KakaoSDKResponse {
        return runCatching {
            authApiService.postKakaoSDK(accessToken)
        }.getOrElse {
            Log.d("AuthRepository", "postKakaoLogin Failed: $it")
            KakaoSDKResponse(
                result = KakaoSDKResult()
            )
        }
    }
}
