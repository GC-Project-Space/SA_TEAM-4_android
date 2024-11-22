package com.chikorita.gamagochi.domain.repository

import android.util.Log
import com.chikorita.gamagochi.data.api.MissionApiService
import com.chikorita.gamagochi.domain.model.Mission
import javax.inject.Inject

class MissionRepository @Inject constructor(
    private val missionApiService: MissionApiService,
) {
    suspend fun getMissionList(): List<Mission> {
        return runCatching {
            missionApiService.getMissionList().missions.map {
                it.convertToModel()
            }
        }.getOrElse {
            Log.d("MissionRepository", "getMissionList Failed: $it")
            emptyList()
        }
    }
}