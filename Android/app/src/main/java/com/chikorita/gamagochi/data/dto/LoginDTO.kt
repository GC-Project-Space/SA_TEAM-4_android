package com.chikorita.gamagochi.data.dto

import com.google.gson.annotations.SerializedName

data class KakaoSDKResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean = false,
    @SerializedName("code") val code: Int = 0,
    @SerializedName("message") val message: String? = null,
    val result: KakaoSDKResult
)

data class KakaoSDKResult(
    var userId: Int = 0,
    var newUser: Boolean = false,
    var accessToken: String = "",
    var refreshToken: String = ""
)

data class SdkTokenBody(
    val kakaoAccessToken: String,
    val kakaoRefreshToken: String
)