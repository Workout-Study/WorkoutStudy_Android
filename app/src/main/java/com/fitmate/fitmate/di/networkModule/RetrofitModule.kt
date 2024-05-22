package com.fitmate.fitmate.di.networkModule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    //로킹 인터셉터 싱글톤
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            // 요청과 응답의 본문 내용까지 로그에 포함
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    //레트로핏 싱글톤
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        //.baseUrl("https://3a5e7e05-ca25-425c-849c-809753f9bc75.mock.pstmn.io/")
        .baseUrl("http://43.200.62.1561:8081/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}