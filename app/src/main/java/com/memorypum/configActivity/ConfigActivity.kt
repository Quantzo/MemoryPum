package com.memorypum.configActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.AppCompatTextView
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener
import java.util.*

class ConfigActivity : AppCompatActivity() {

    private var configActivityGestureListener: GestureDetectorCompat? = null
    var textToSpeech: TextToSpeech? = null
    var numberOfPairs = 0
        get
        set(value) {
            if (value < 2) {
                field = 2
                vibrate()
            } else if (value > 50) {
                field = 50
                vibrate()
            } else {
                field = value
                textToSpeech?.speak(value.toString(), TextToSpeech.QUEUE_FLUSH, null)
            }
            textView?.text = field.toString()
        }

    var textView: AppCompatTextView? = null
        private set
        get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        configActivityGestureListener = GestureDetectorCompat(this, ConfigActivityGestures(this))

        textView = findViewById(R.id.textConfig) as AppCompatTextView
        numberOfPairs = intent.getIntExtra("Number_Of_Pairs",8)

        textToSpeech = TextToSpeech(applicationContext, {status ->  if(status != TextToSpeech.ERROR){ textToSpeech?.language = Locale.getDefault()}} )


    }


    fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (v.hasVibrator()) v.vibrate(400)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        configActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun finish() {
        val intent = Intent()
        intent.putExtra("Number_Of_Pairs",numberOfPairs)
        setResult(Activity.RESULT_OK, intent)
        super.finish()
    }


    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }

    override fun onPause() {
        textToSpeech?.stop()
        super.onPause()
    }



    class ConfigActivityGestures(context: AppCompatActivity) : AppGesturesListener(context)
    {

        //Pairs Number Increase
        override fun onSwipeUp(){
            (context as ConfigActivity).numberOfPairs += 1
        }
        //Pairs Number Decrease
        override fun onSwipeDown(){
            (context as ConfigActivity).numberOfPairs -= 1
        }
        //Return to main
        override fun onLongPress(){
            context.finish()
        }


        override fun onSwipeLeft(){}
        override fun onSwipeRight(){}
        override fun onDoubleTouch(){}
        override fun onSingleTouch(){}
    }
}
