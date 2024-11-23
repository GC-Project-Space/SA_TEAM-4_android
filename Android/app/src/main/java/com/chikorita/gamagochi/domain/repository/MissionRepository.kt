package com.chikorita.gamagochi.domain.repository

import android.util.Log
import com.chikorita.gamagochi.data.api.MissionApiService
import com.chikorita.gamagochi.data.dto.MissionClearRequestBody
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

    suspend fun clearMission(missionId: Long): Boolean {
        return runCatching {
            missionApiService.clearMission(MissionClearRequestBody(missionId)).status == SUCCESS_MESSAGE
        }.getOrElse {
            Log.d("MissionRepository", "clearMission Failed: $it")
            false
        }
    }

    companion object {
        const val SUCCESS_MESSAGE = "success"
    }
}