package com.memorypum.gameActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener

class GameActivity : AppCompatActivity() {

    private var gameActivityGestureListener: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameActivityGestureListener = GestureDetectorCompat(this, GameActivityGestures())
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gameActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    class GameActivityGestures : AppGesturesListener()
    {
        //Move Right
        override fun onSwipeLeft(){}
        //Move Left
        override fun onSwipeRight(){}
        //Move Down
        override fun onSwipeUp(){}
        //Move Up
        override fun onSwipeDown(){}
        //Reveal
        override fun onDoubleTouch(){}
        //Return to main
        override fun onLongPress(){}

        override fun onSingleTouch(){}
    }
}
