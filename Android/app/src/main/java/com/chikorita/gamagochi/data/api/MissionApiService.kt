package com.chikorita.gamagochi.data.api

import com.chikorita.gamagochi.data.dto.MissionClearRequestBody
import com.chikorita.gamagochi.data.dto.MissionResponse
import com.chikorita.gamagochi.presentation.config.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MissionApiService {
    // 미션 목록 조회
    @GET("api/mission/progress")
    suspend fun getMissionList() : MissionResponse

    // 미션 달성
    @POST("api/mission/clear")
    suspend fun clearMission(
        @Body missionClearRequest: MissionClearRequestBody
    ) : BaseResponse
}