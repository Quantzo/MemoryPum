package com.memorypum.gameActivity

import java.util.*


class GameMap(numberOfPairs: Int) {
    private val map = mutableListOf<CardItem>()

    init {
        val ids = ((1..numberOfPairs).flatMap { listOf(it, it) }).sortedBy { it < Random().nextInt(numberOfPairs + 1) }
        ids.forEach { item -> map.add(CardItem(item)) }
    }

    fun canReveal(position: Int): Boolean {
        return map[position].status != CardItem.CardStatus.DONE
    }

    fun isAnyPairsLeft(): Boolean {
        return map.filter { it.status == CardItem.CardStatus.HIDDEN }.isNotEmpty()
    }

    fun pairDone(id: Int) {
        map.filter { it.Id == id }.forEach { it.status = CardItem.CardStatus.DONE }
    }

    fun getCardIdFromPosition(position: Int): Int {
        return map[position].Id
    }

    fun getCardIdIfRevealed(position: Int): Int {
        if (map[position].status == CardItem.CardStatus.DONE) {
            return map[position].Id
        } else
            return 0
    }


    class CardItem(val Id: Int) {
        var status = CardStatus.HIDDEN
            get
            set

        enum class CardStatus {
            HIDDEN, DONE
        }
    }
}