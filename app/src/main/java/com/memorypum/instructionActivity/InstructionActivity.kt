package com.memorypum.instructionActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener

class InstructionActivity : AppCompatActivity() {

    private var instructionActivityGestureListener: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)
        instructionActivityGestureListener = GestureDetectorCompat(this, InstructionActivityGestures(this))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        instructionActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }


    class InstructionActivityGestures(context: AppCompatActivity) : AppGesturesListener(context)
    {
        //Dismiss/next
        override fun onSwipeLeft(){}
        //Previous
        override fun onSwipeRight(){}
        //Repeat
        override fun onSwipeUp(){}
        //Return to main
        override fun onLongPress(){
            context.finish()
        }


        override fun onSwipeDown(){}
        override fun onDoubleTouch(){}
        override fun onSingleTouch(){}
    }
}
