package com.chikorita.gamagochi.domain.repository

import android.util.Log
import com.chikorita.gamagochi.data.api.RankApiService
import com.chikorita.gamagochi.data.dto.LadybugDetailResponse
import com.chikorita.gamagochi.data.dto.LadybugDetailResult
import com.chikorita.gamagochi.data.dto.LadybugLocationRequest
import com.chikorita.gamagochi.data.dto.LevelRankingResponse
import com.chikorita.gamagochi.data.dto.RankingResult
import com.chikorita.gamagochi.data.dto.SuccessMissionResponse
import com.chikorita.gamagochi.data.dto.SuccessMissionResult
import com.chikorita.gamagochi.data.dto.GetAllMajorResponse
import javax.inject.Inject

class RankRepository @Inject constructor(
    private val rankApiService: RankApiService,
) {

    suspend fun getLevelRanking(): LevelRankingResponse {
        return runCatching {
            rankApiService.getLevelRanking()
        }.getOrElse {
            Log.d("RankRepository", "getLevelRanking Failed: $it")
            LevelRankingResponse(RankingResult(emptyList()))
        }
    }

    suspend fun getMajorAll(): GetAllMajorResponse {
        return runCatching {
            rankApiService.getMajorAll()
        }.getOrElse {
            Log.d("RankRepository", "getMajorAll Failed: $it")
            return GetAllMajorResponse(emptyList())
        }
    }

    suspend fun postLocation(
        ladybugLocationRequest : LadybugLocationRequest
    ): SuccessMissionResponse {
        return runCatching {
            rankApiService.postLadyBugLocation(ladybugLocationRequest, USER_ID)
        }.getOrElse {
            Log.d("RankRepository", "postLocation Failed: $it")
            SuccessMissionResponse(result = SuccessMissionResult(emptyList()))
        }
    }

    suspend fun getLadybugDetail(): LadybugDetailResponse {
        return runCatching {
            rankApiService.getLadybugDetail(USER_ID)
        }.getOrElse {
            Log.d("RankRepository", "getLadybugDetail Failed: $it")
            LadybugDetailResponse(LadybugDetailResult())
        }
    }

    companion object {
        const val USER_ID: Long = 17 //TODO: 임시 유저 id
    }
}