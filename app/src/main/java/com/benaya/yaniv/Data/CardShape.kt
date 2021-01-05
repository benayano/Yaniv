package com.benaya.yaniv.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CardShape {
    @SerialName("joker")
    JOKER,
    @SerialName("spades")
    SPADES,
    @SerialName("hearts")
    HEARTS,
    @SerialName("diamonds")
    DIAMONDS,
    @SerialName("clubs")
    CLUBS
}