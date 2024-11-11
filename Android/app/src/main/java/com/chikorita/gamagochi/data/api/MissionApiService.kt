package com.chikorita.gamagochi.data.api

import com.chikorita.gamagochi.data.dto.MissionResponse
import retrofit2.http.GET

interface MissionApiService {
    // 미션 받아오기
    @GET("mission")
    fun getMission() : MissionResponse
}