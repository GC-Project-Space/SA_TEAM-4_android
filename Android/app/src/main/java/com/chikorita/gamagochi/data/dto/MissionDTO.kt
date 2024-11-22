package com.chikorita.gamagochi.data.dto

import com.chikorita.gamagochi.domain.model.Mission
import com.kakao.vectormap.LatLng

data class MissionResponse(
    val status: String,
    val missions: List<MissionResult>
)

// 지도에 표시되는 미션
data class MissionResult(
    var missionId: Long,
    var missionName: String? = null, // 미션 이름
    var coordinate: String, // 좌표 (위도, 경도)
    var cleared: Boolean
) {
    fun convertToModel(): Mission {
        val parts = coordinate.split(",")
        val latLng = LatLng.from(parts[0].toDouble(),  parts[1].toDouble())
        return Mission(
            missionId = this.missionId,
            missionName = this.missionName,
            latLng = latLng,
            isFinished = this.cleared
        )
    }
}