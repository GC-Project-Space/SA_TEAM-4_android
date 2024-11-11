package com.chikorita.gamagochi.presentation.di

import com.chikorita.gamagochi.data.api.AuthApiService
import com.chikorita.gamagochi.data.api.RankApiService
import com.chikorita.gamagochi.domain.repository.AuthRepository
import com.chikorita.gamagochi.domain.repository.RankRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    /** 인증 */
    @Provides
    fun provideAuthRepository(
        authApiService: AuthApiService,
    ): AuthRepository = AuthRepository(authApiService)

    /** 랭킹 */
    @Provides
    fun provideRankRepository(
        rankApiService: RankApiService,
    ): RankRepository = RankRepository(rankApiService)
}