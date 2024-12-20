package com.chikorita.gamagochi.presentation.di

import android.content.Context
import com.chikorita.gamagochi.presentation.config.ApplicationClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): ApplicationClass {
        return app as ApplicationClass
    }
}
