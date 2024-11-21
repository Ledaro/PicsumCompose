package com.zeltech.picsumcompose.di

import com.zeltech.picsumcompose.data.remote.PicsumApi
import com.zeltech.picsumcompose.data.repository.PicsumRepositoryImpl
import com.zeltech.picsumcompose.domain.repository.PicsumRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PicsumApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePicsumApi(retrofit: Retrofit): PicsumApi {
        return retrofit.create(PicsumApi::class.java)
    }

    @Provides
    @Singleton
    fun providePicsumRepository(api: PicsumApi): PicsumRepository = PicsumRepositoryImpl(api)
}
