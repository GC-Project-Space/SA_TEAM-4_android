package com.chikorita.gamagochi.data.api

import com.chikorita.gamagochi.data.dto.RefreshRequest
import com.chikorita.gamagochi.data.dto.RefreshResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApiService {
    // 토큰 재발급
    @POST("auth/tokens/refresh")
    suspend fun refreshToken(
        @Body body: RefreshRequest
    ): RefreshResponse
}