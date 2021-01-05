package com.benaya.yaniv.model.network

import com.benaya.yaniv.Data.Card
import com.benaya.yaniv.Data.Player
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: Int,
    val currentPlayer: Int,
    val deck: Deck,
    val players: List<Player>,
    val isLive:Boolean
)

@Serializable
data class Deck(
    val open: Card,
    val inDeck: List<Card>
)
