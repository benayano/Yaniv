package com.benaya.yaniv.model.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GameApiService {
    @GET("/games/{id}")
    fun getGameStatus(@Path("id") id: Int, @Header("apikey") apikey: String): Call<Game>

    @POST("/games")
        fun postGamesStatus(@Header("apikey") apikey: String): Call<Game>
//
//    @POST("/moove")
//    fun replaceTurn(@path("id")id:Int,@Body("body") body:Body):Call<Game>

}
