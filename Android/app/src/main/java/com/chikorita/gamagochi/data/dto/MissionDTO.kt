package com.chikorita.gamagochi.data.dto

import com.chikorita.gamagochi.presentation.config.base.BaseResponse

data class MissionResponse(
    val result: MissionMapResult
) : BaseResponse()

// 미션 목록
data class MissionMapResult(
    val missionList: ArrayList<Mission>
)

// 지도에 표시되는 미션
data class Mission(
    var missionId: Long,
    var missionName: String? = null, // 미션 이름
    var latitude: Double, // 위도
    var longitude: Double // 경도
)