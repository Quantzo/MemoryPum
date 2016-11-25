package com.memorypum.mainActivity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.view.GestureDetectorCompat
import android.util.Log
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener
import com.memorypum.common.RequestCodes
import com.memorypum.configActivity.ConfigActivity
import com.memorypum.gameActivity.GameActivity
import com.memorypum.instructionActivity.InstructionActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mainActivityGestureListener:GestureDetectorCompat? = null
    private var preferences:SharedPreferences? = null
    var numberOfPairs:Int = 0
        get
        private set
    var textToSpeech:TextToSpeech? = null
        get
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityGestureListener = GestureDetectorCompat(this, MainActivityGestures(this))

        preferences = this.getSharedPreferences("Game_Config",android.content.Context.MODE_PRIVATE)
        numberOfPairs = preferences?.getInt("Number_Of_Pairs", 8) ?: 8

        textToSpeech = TextToSpeech(applicationContext, {status ->  if(status != TextToSpeech.ERROR){ textToSpeech?.language = Locale.getDefault(); playInstructions()}} )


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mainActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK)
        {
            when(requestCode)
            {
                RequestCodes.CONFIG_ACTIVITY.value -> numberOfPairs = data?.getIntExtra("Number_Of_Pairs", numberOfPairs) ?: numberOfPairs
                RequestCodes.GAME_ACTIVITY.value -> playResults(data?.getIntExtra("Game_Result", 0) ?: 0)
                else -> Log.w("MyActivity", "Undefined Request Code")
            }

            putIntInSharedProperties("Number_Of_Pairs", numberOfPairs)

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun putIntInSharedProperties(key:String, value:Int)
    {
        val editor = preferences?.edit()
        editor?.putInt(key,value)
        editor?.apply()
    }

    private fun playResults(points: Int) {

        textToSpeech?.speak(resources.getString(R.string.main_menu_score, points), TextToSpeech.QUEUE_FLUSH, null, "MainMenuResults")
    }

    private fun playInstructions() {
        textToSpeech?.speak(resources.getString(R.string.main_menu_instructions), TextToSpeech.QUEUE_FLUSH, null, "MainMenuInstructions")
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

    class MainActivityGestures(context: AppCompatActivity) : AppGesturesListener(context)
    {
        //game
        override fun onSwipeLeft(){
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("Number_Of_Pairs",(context as MainActivity).numberOfPairs)
            context.startActivityForResult(intent, RequestCodes.GAME_ACTIVITY.value)
        }
        //config
        override fun onSwipeRight(){
            val intent = Intent(context, ConfigActivity::class.java)
            intent.putExtra("Number_Of_Pairs",(context as MainActivity).numberOfPairs)
            context.startActivityForResult(intent, RequestCodes.CONFIG_ACTIVITY.value)
        }
        //instruction
        override fun onDoubleTouch(){
            val intent = Intent(context, InstructionActivity::class.java)
            context.startActivity(intent)
        }
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
