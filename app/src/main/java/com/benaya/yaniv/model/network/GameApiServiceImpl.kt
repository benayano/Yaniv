package com.benaya.yaniv.model.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

object GameApiServiceImpl {
    val service: GameApiService by lazy { getRetrofit().create(GameApiService::class.java)}
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://androidoss-yaniv.herokuapp.com")
            .addConverterFactory(
                Json { ignoreUnknownKeys = true}
                    .asConverterFactory(MediaType.get("application/json"))
            )
            .build()
    }
}