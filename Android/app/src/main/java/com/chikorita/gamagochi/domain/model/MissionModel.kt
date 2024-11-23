package com.chikorita.gamagochi.domain.model

import com.kakao.vectormap.LatLng

data class Mission(
    var missionId: Long,
    var missionName: String? = null, // 미션 이름
    var latLng: LatLng, // 좌표
    var isFinished: Boolean
)

enum class MissionStatus() {
    UNABLE, ABLE, DONE
}