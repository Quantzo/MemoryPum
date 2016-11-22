package com.memorypum.gameActivity

class Game(val numberOfPairs: Int) {
    private var currentItem = 0
        set(value) {
            field = value
            computeCurrentRow(value)
        }
    private var currentRow = 0
    private val gameMap = GameMap(numberOfPairs)
    private var currentlyRevealedCard: Int? = null
    private var currentlyRevealedCardPos: Int = -1


    fun canReveal(): Boolean {
        return gameMap.canReveal(currentItem) && currentItem != currentlyRevealedCardPos
    }

    fun getCurrentIdIfRevealed(): MapPosition {
        val res = gameMap.getCardIdIfRevealed(currentItem)
        if (res == 0) {
            if (currentlyRevealedCardPos == currentItem) {
                return MapPosition(currentItem, currentlyRevealedCard!!)
            } else {
                return MapPosition(currentItem, 0)
            }
        } else {
            return MapPosition(currentItem, res)
        }

    }

    fun isGameOver(): Boolean {
        return gameMap.isAnyPairsLeft()
    }

    fun reveal(): RevealResult {
        if (currentlyRevealedCard == null) {
            currentlyRevealedCard = gameMap.getCardIdFromPosition(currentItem)
            currentlyRevealedCardPos = currentItem
            return RevealResult(true, currentlyRevealedCard!!, 0, false)
        } else {
            val currentId = gameMap.getCardIdFromPosition(currentItem)
            if (currentlyRevealedCard!! == currentId) {
                gameMap.pairDone(currentId)
                currentlyRevealedCard = null
                currentlyRevealedCardPos = -1
                return RevealResult(false, currentId, currentId, true)
            } else {
                val returnVal = RevealResult(false, currentlyRevealedCard!!, currentId, false)
                currentlyRevealedCard = null
                currentlyRevealedCardPos = -1
                return returnVal

            }
        }

    }


    fun move(func: (currentPosition: Int, minMax: Int) -> Boolean, minMax: (currentRow: Int, maxNumber: Int) -> Int, moveAct: (currentPosition: Int) -> Int): Boolean {

        val canMove = func(currentItem, minMax(currentRow, (numberOfPairs * 2) - 1))

        if (canMove) {
            currentItem = moveAct(currentItem)
        }
        return canMove

    }

    private fun computeCurrentRow(position: Int) {
        currentRow = position / 5
    }


    data class MapPosition(val Position: Int, val Id: Int)
    data class RevealResult(val FirstCard: Boolean, val CardId1: Int, val CardId2: Int, val Result: Boolean)


}