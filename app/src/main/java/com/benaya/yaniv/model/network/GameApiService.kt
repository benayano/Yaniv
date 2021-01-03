package com.benaya.yaniv.model.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GameApiService {
    @GET("games/")
    fun fetchGameStatus(@Path("id") id: Int, @Header("apikey") apikey: String): Call<GameResponse>
}
