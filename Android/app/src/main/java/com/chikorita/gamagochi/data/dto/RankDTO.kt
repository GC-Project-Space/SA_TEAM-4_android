package com.chikorita.gamagochi.data.dto

import com.chikorita.gamagochi.presentation.config.base.BaseResponse

/** 내 랭킹 정보 조회 (바텀시트) */
data class LadybugDetailResponse (
    val result: LadybugDetailResult
): BaseResponse()

data class LadybugDetailResult(
    val levelInfo: LevelInfo = LevelInfo(), // 레벨 정보
    val majorType : String = "ARTIFICIAL_INTELLIGENCE", // 학과
    val majorRank: List<MajorRank> = emptyList(), // 학과 내 나의 랭킹
    val schoolRank : List<SchoolRank> = emptyList(), // 학교 내 나의 랭킹
    val symbol: String = "Bronze"
)

// 레벨 정보
data class LevelInfo(
    val experience: Int = 0, // 경험치
    val ladybugType : String = "EGG", // 진화 단계
    val level : Int = 0, // 레벨
)

// 학과 랭킹
data class MajorRank(
    val experience: Int = 0,
    val majorType : String = "ARTIFICIAL_INTELLIGENCE",
    val rank : Int = 0,
)

// 학교 랭킹
data class SchoolRank(
    val experience: Int = 0, // 경험치
    val ladybugType : String = "EGG", // 레벨
    val nickname : String = "string", // 닉네임
    val rank : Int = 0 // 순위
)

/** 랭킹 조회 */
data class LevelRankingResponse (
    val result: RankingResult
): BaseResponse()

data class RankingResult(
    val rankingList: List<RankingList>
)

data class RankingList(
    val experience: Int = 0,
    val ladybugType: String = "",
    val nickName: String = "",
    val rank: Int = 0
)

/** */
data class LadybugLocationRequest (
    val latitude: Double,
    val longitude: Double,
    val missionIdList: List<Long>
)

data class SuccessMissionResponse(
    val result: SuccessMissionResult
): BaseResponse()

data class SuccessMissionResult(
    val successMission: List<Long>
)