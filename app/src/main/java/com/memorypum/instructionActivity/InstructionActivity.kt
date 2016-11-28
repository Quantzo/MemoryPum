package com.memorypum.instructionActivity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.v4.view.GestureDetectorCompat
import android.util.Log
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener
import java.util.*

class InstructionActivity : AppCompatActivity() {

    private var instructionActivityGestureListener: GestureDetectorCompat? = null
    var textToSpeech: TextToSpeech? = null
        get
        private set

    var audioListener: InstructionProgressAudioListener? = null

    var currentIndex:Int = 0
        get
        set
    val textLog = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTextList()

        setContentView(R.layout.activity_instruction)
        instructionActivityGestureListener = GestureDetectorCompat(this, InstructionActivityGestures(this))

        audioListener = InstructionProgressAudioListener(this)

        textToSpeech = TextToSpeech(applicationContext, {status ->  if(status != TextToSpeech.ERROR){ textToSpeech?.language = Locale.getDefault(); textToSpeech?.setOnUtteranceProgressListener(audioListener); playCurrentItem() }} )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        instructionActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
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

    fun initTextList()
    {
        textLog.add(resources.getString(R.string.instruction_instruction))
        textLog.add(resources.getString(R.string.instruction_config))
        textLog.add(resources.getString(R.string.instruction_game))
        textLog.add(resources.getString(R.string.instruction_gameRules))
    }

    private fun playCurrentItem()
    {
        textToSpeech?.speak(textLog[currentIndex], TextToSpeech.QUEUE_FLUSH, null, "Instructions_$currentIndex")

    }

    fun nextText()
    {
        if (currentIndex < textLog.size) {
            currentIndex += 1
            if(currentIndex < textLog.size) playCurrentItem()
        } else vibrate()

    }

    fun previousText()
    {
        if (currentIndex == textLog.size) {
            currentIndex -= 2
        }
        playCurrentItem()
    }

    fun repeatText()
    {
        if (currentIndex == textLog.size) {
            currentIndex -= 1
        }
        playCurrentItem()
    }


    fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (v.hasVibrator()) v.vibrate(400)
    }



    class InstructionProgressAudioListener(val activity:InstructionActivity) : UtteranceProgressListener()
    {

        override fun onStart(utteranceId: String?) {
        }
        override fun onDone(utteranceId: String?) {
            activity.nextText()
        }
        override fun onError(utteranceId: String?, errorCode:Int) {
            Log.e("Ins onError", "$utteranceId errorCode:$errorCode" )
        }
        override fun onError(utteranceId: String?) {
            Log.e("Ins onError deprecated", utteranceId )
        }

    }


    class InstructionActivityGestures(context: InstructionActivity) : AppGesturesListener(context)
    {
        //Dismiss/next
        override fun onSwipeLeft(){
            (context as InstructionActivity).nextText()
        }
        //Previous
        override fun onSwipeRight(){
            (context as InstructionActivity).previousText()
        }
        //Repeat
        override fun onSwipeUp(){
            (context as InstructionActivity).repeatText()
        }
        //Return to main
        override fun onLongPress(){
            context.finish()
        }


        override fun onSwipeDown(){}
        override fun onDoubleTouch(){}
        override fun onSingleTouch(){}
    }
}
