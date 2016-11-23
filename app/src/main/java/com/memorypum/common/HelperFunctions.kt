package com.memorypum.common

import java.util.*

fun MutableList<String>.shuffle():List<String>
{
    val rg : Random = Random()
    for (i in 0..size - 1) {
        val randomPosition = rg.nextInt(size)
        val tmp  = this[i]
        this[i] = this[randomPosition]
        this[randomPosition] = tmp
    }
    return this
}


