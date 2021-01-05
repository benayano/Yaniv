package com.benaya.yaniv.Data

import kotlinx.serialization.Serializable

@Serializable
data class Player(val userId: Int, val name:String, val cards: List<Card>, val score:Int)
