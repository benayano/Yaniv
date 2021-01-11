package com.benaya.yaniv.Data

import kotlinx.serialization.Serializable

@Serializable
data class Card(val value: Int, val suit: CardShape) {
     fun formatted(): String {
        var formattedShape = ""
        val rows = Math.sqrt(value.toDouble()).toInt()
        val cols = value / rows

        for (r in 1..rows) {
            for (c in 1..cols) {
                formattedShape += suit.toString()
            }
            formattedShape += "\n"
        }
        for (i in 1..(value - rows * cols))
            formattedShape += suit.toString()

        return formattedShape
    }
}