package com.memorypum.common

import android.view.GestureDetector
import android.view.MotionEvent


abstract class AppGesturesListener() : GestureDetector.SimpleOnGestureListener() {

    private val swipeThreshold:Int = 100
    private val velocityThreshold:Int = 100



    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
    abstract fun onSwipeUp()
    abstract fun onSwipeDown()
    abstract fun onDoubleTouch()
    abstract fun onSingleTouch()
    abstract fun onLongPress()



    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        val distanceY = floatMinus(e2?.y, e1?.y)
        val distanceX = floatMinus(e2?.x, e1?.x)

        if(distanceX != null && distanceY != null)
        {
            if(Math.abs(distanceX) > Math.abs(distanceY))
            {
                if(checkValues(distanceX, velocityX))
                {
                    if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                    return true
                }
            }
            else
            {
                if(checkValues(distanceY, velocityY))
                {
                    if (distanceY > 0) onSwipeDown() else onSwipeUp()
                    return true
                }
            }
        }
        return false
    }


    override fun onDoubleTap(e: MotionEvent?): Boolean {
        onDoubleTouch()
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        onSingleTouch()
        return true
    }

    private fun floatMinus(x: Float?, y: Float?): Float? {
        if (x != null && y != null)
            return (x - y)
        else
            return null

    }

    private fun checkValues(distance: Float, velocity: Float): Boolean
    {
        return (Math.abs(distance)> swipeThreshold && Math.abs(velocity) > velocityThreshold)
    }

    override fun onLongPress(e: MotionEvent?) {
        onLongPress()
    }


}