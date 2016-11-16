package com.memorypum.mainActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener


class MainActivity : AppCompatActivity() {

    private var mainActivityGestureListener:GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityGestureListener = GestureDetectorCompat(this, MainActivityGestures())

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mainActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }



    class MainActivityGestures : AppGesturesListener()
    {
        //game
        override fun onSwipeLeft(){}
        //config
        override fun onSwipeRight(){}
        //instruction
        override fun onDoubleTouch(){}
        //exit
        override fun onLongPress(){
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }


        override fun onSwipeUp(){}
        override fun onSwipeDown(){}
        override fun onSingleTouch(){}


    }
}
