package com.benaya.yaniv.model.network

import retrofit2.Call
import retrofit2.http.*

interface GameApiService {
    @GET("/games/{id}")
    fun getGameStatus(@Path("id") id: Int, @Header("apikey") apikey: String): Call<Game>

    @POST("/games")
        fun postGamesStatus(@Header("apikey") apikey: String): Call<Game>

    @POST("/move")
       fun replaceTurn(@Header("apikey") apikey: String,@Body() body:BodyMove):Call<Game>

}
