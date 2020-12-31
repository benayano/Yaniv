package com.benaya.yaniv.Data

class Card(val value: Int, val shape: CardShape) {
    override fun toString(): String {
        var formattedShape = ""
        val rows = Math.sqrt(value.toDouble()).toInt()
        val cols = value / rows

        for (r in 1..rows) {
            for (c in 1..cols) {
                formattedShape += shape.toString()
            }
            formattedShape += "\n"
        }
        for (i in 1..(value - rows * cols))
            formattedShape += shape.toString()

        return formattedShape
    }
}