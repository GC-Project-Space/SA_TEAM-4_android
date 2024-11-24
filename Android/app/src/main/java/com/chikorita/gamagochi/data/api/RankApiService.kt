package com.chikorita.gamagochi.data.api

import com.chikorita.gamagochi.data.dto.LadybugDetailResponse
import com.chikorita.gamagochi.data.dto.LadybugLocationRequest
import com.chikorita.gamagochi.data.dto.LevelRankingResponse
import com.chikorita.gamagochi.data.dto.SuccessMissionResponse
import com.chikorita.gamagochi.data.dto.GetAllMajorResponse
import com.chikorita.gamagochi.data.dto.RankingList
import com.chikorita.gamagochi.domain.model.MajorName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RankApiService {
    // 미션 클리어
    @POST("ladybug")
    suspend fun postLadyBugLocation(
        @Body request : LadybugLocationRequest,
        @Query("userId") userId: Long
    ): SuccessMissionResponse

    @GET("ladybug/detail")
    suspend fun getLadybugDetail(
        @Query("userId") userId: Long
    ): LadybugDetailResponse

    @GET("api/ranking/all")
    suspend fun getLevelRanking(): List<RankingList>


    @GET("api/ranking/major")
    suspend fun getMajorAll() : GetAllMajorResponse

    @GET("level/names")
    suspend fun getMajorNames() : MajorName
}