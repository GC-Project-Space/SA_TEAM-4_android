package com.chikorita.gamagochi.data.api

import com.chikorita.gamagochi.data.dto.KakaoSDKResponse
import com.chikorita.gamagochi.data.dto.GetMajorListResult
import com.chikorita.gamagochi.data.dto.SetUserInfoRequest
import com.chikorita.gamagochi.data.dto.SetUserInfoResponse
import com.chikorita.gamagochi.domain.model.RegisterInfoModel
import com.chikorita.gamagochi.presentation.config.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    // SDK 카카오 로그인
    @POST("auth/kakao-login")
    fun postKakaoSDK(
        @Query("accessToken") accessToken: String?
    ) : KakaoSDKResponse

    // 회원가입
    @POST("auth/join")
    suspend fun postJoin(
        @Body request: RegisterInfoModel
    ) : BaseResponse

    // 회원정보 세팅
    @PATCH("user/update")
    fun patchUserUpdate(
        @Body request: SetUserInfoRequest
    ): SetUserInfoResponse

    // 학과 목록 조회
    @GET("major/names")
    fun getMajorList(): GetMajorListResult
}