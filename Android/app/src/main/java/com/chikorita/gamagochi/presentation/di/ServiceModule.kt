package com.chikorita.gamagochi.presentation.di

import com.chikorita.gamagochi.data.api.AuthApiService
import com.chikorita.gamagochi.data.api.MissionApiService
import com.chikorita.gamagochi.data.api.RankApiService
import com.chikorita.gamagochi.data.api.RefreshApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    /** 인증 (로그아웃, 회원탈퇴) */
    @Provides
    @Singleton
    fun provideAuthService(@NetworkModule.BasicRetrofit retrofit: Retrofit) : AuthApiService =
        retrofit.create(AuthApiService::class.java)

    /** 토큰 재발급 */
    @Provides
    @Singleton
    fun provideRefreshService(@NetworkModule.RefreshRetrofit retrofit: Retrofit) : RefreshApiService =
        retrofit.create(RefreshApiService::class.java)

    /** 미션 */
    @Provides
    @Singleton
    fun provideMissionService(@NetworkModule.BasicRetrofit retrofit: Retrofit) : MissionApiService =
        retrofit.create(MissionApiService::class.java)

    /** 랭킹 */
    @Provides
    @Singleton
    fun provideRankService(@NetworkModule.BasicRetrofit retrofit: Retrofit) : RankApiService =
        retrofit.create(RankApiService::class.java)
}