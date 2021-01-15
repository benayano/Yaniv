package com.benaya.yaniv.model.network

import com.benaya.yaniv.data.Card
import com.benaya.yaniv.data.Player
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


@Serializable
data class BodyMove(
    val playerId: Int,
   val gameId: Int,
   val droppedCards: List<Card>,
    val takeCardFrom: TakeCardFrom
)

@Serializable
enum class TakeCardFrom {
    Open,
    Deck
}
