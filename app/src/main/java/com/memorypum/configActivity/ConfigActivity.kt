package com.memorypum.configActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener

class ConfigActivity : AppCompatActivity() {

    private var configActivityGestureListener: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        configActivityGestureListener = GestureDetectorCompat(this, ConfigActivityGestures())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        configActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    class ConfigActivityGestures : AppGesturesListener()
    {

        //Pairs Number Increase
        override fun onSwipeUp(){}
        //Pairs Number Decrease
        override fun onSwipeDown(){}
        //Return to main
        override fun onLongPress(){}


        override fun onSwipeLeft(){}
        override fun onSwipeRight(){}
        override fun onDoubleTouch(){}
        override fun onSingleTouch(){}
    }
}
