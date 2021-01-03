package com.benaya.yaniv.model.network

import com.benaya.yaniv.Data.Card
import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(val gameData: GameData)

@Serializable
data class GameData(
    val id: Int,
    val currentPlayer: Int,
    val deck: Deck,
    val isAlive:Boolean
)

@Serializable
data class Deck(
    val open: Card,
    val inDeck: List<Card>
    )
