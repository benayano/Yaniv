package com.benaya.yaniv.model.network

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(val gameData: GameData)

@Serializable
data class GameData(
    val id: Int,
    val currentPlayer: Int,

    val isAlive:Boolean
)


