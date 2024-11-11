package com.chikorita.gamagochi.data.dto

// 회원가입
data class SetUserInfoRequest(
    val userId: Int,
    val newUsername: String,
    val newMajor: String = "ARTIFICIAL_INTELIGENCE"
)

data class SetUserInfoResponse(
    val result: SetUserInfoResult
)

data class SetUserInfoResult(
    val succeed: Boolean
)

data class GetMajorListResponse(
    val result: GetMajorListResult
)

data class GetMajorListResult(
    val majorList: ArrayList<String>
)

// 토큰 재발급
data class RefreshRequest(
    val refreshToken: String,
)

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)