package com.chikorita.gamagochi.data.api

import com.chikorita.gamagochi.data.dto.MissionResponse
import retrofit2.http.GET

interface MissionApiService {
    // 미션 목록 조회
    @GET("api/mission/progress")
    suspend fun getMissionList() : MissionResponse
}