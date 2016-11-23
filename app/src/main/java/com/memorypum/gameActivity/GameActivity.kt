package com.memorypum.gameActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener
import com.memorypum.common.shuffle
import java.util.*


class GameActivity : AppCompatActivity() {

    private var gameActivityGestureListener: GestureDetectorCompat? = null

    var game: Game? = null
        get
        private set

    private var points: Int = 0

    var textView: AppCompatTextView? = null
        private set
        get

    val idNamesList = mutableListOf<String>()
    var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val numberOfPairs = intent.getIntExtra("Number_Of_Pairs", 8)
        gameActivityGestureListener = GestureDetectorCompat(this, GameActivityGestures(this))
        game = Game(numberOfPairs)

        textView = findViewById(R.id.textGame) as AppCompatTextView
        textView?.text = game?.getCurrentIdIfRevealed().toString()


        initializeList()

        textToSpeech = TextToSpeech(applicationContext, {status ->  if(status != TextToSpeech.ERROR){ textToSpeech?.language = Locale.getDefault()}} )



    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gameActivityGestureListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun finish() {
        val intent = Intent()
        intent.putExtra("Game_Result", points)
        setResult(Activity.RESULT_OK, intent)
        super.finish()
    }

    fun vibrate()
    {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if(v.hasVibrator())v.vibrate(400)
    }

    private fun initializeList()
    {

        idNamesList.addAll(resources.getStringArray(R.array.game_gameIds))
        idNamesList.shuffle()
    }


    fun firstCardReveal(id:Int)
    {
        val idName = idNamesList[id-1]
        textToSpeech?.speak(idName, TextToSpeech.QUEUE_ADD, null, "Game_FirstCardReveal_$id")
    }
    fun failPairReveal(id1:Int, id2:Int)
    {
        val idName1 = idNamesList[id1-1]
        val idName2 = idNamesList[id2-1]

        val res = resources.getString(R.string.game_second_card_fail)

        textToSpeech?.speak(idName1 + res + idName2, TextToSpeech.QUEUE_ADD, null, "Game_Second_Fail_${id1}_$id2")
    }
    fun succesPairReveal(id:Int)
    {
        val idName = idNamesList[id-1]
        val res = resources.getString(R.string.game_second_card_success)
        textToSpeech?.speak(idName + res, TextToSpeech.QUEUE_ADD, null, "Game_Second_Success_$id")
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


    class GameActivityGestures(context: AppCompatActivity) : AppGesturesListener(context)
    {
        //Move Right
        override fun onSwipeLeft() {
            val result = (context as GameActivity).game?.move({ p, mF -> p + 1 <= mF }, {r, m -> Math.min(m, (5 * r) + 4) }, { p -> p + 1 }) ?: false

            if (!result) {
                context.vibrate()
                Log.d("GameActivitySwipeLeft", "Vibrated")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }


        }
        //Move Left
        override fun onSwipeRight() {
            val result = (context as GameActivity).game?.move({ p, mF -> p - 1 >= mF }, { r, m -> r * 5 }, { p -> p - 1 }) ?: false
            if (!result) {
                context.vibrate()
                Log.d("GameActivitySwipeRight", "Vibrated")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }
        }
        //Move Down
        override fun onSwipeUp() {
            val result = (context as GameActivity).game?.move({ p, mF -> p + 5 <= mF }, {r, m -> m }, { p -> p + 5 }) ?: false
            if (!result) {
                context.vibrate()
                Log.d("GameActivitySwipeUp", "Vibrated")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }
        }
        //Move Up
        override fun onSwipeDown() {
            val result = (context as GameActivity).game?.move({ p, mF -> p - 5 >= mF }, {r, m -> 0 }, { p -> p - 5 }) ?: false
            if (!result) {
                context.vibrate()
                Log.d("GameActivitySwipeDown", "Vibrated")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }
        }
        //Reveal
        override fun onDoubleTouch() {
            val result = (context as GameActivity).game?.canReveal() ?: false
            if (!result) {
                context.vibrate()
                Log.d("GameActivityDoubleTouch", "Vibrated")
            } else {
                val (firstCard, firstCardId, secondCardId, resultOfSecondCardReveal) = context.game?.reveal() ?: Game.RevealResult(false, 0, 0, false)
                if (firstCard) {
                    context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
                    context.firstCardReveal(firstCardId)
                } else {
                    if (resultOfSecondCardReveal) {
                        context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
                        context.points += 2
                        if(context.game?.isGameOver() ?: false)context.finish()
                        context.succesPairReveal(firstCardId)

                    } else {
                        context.failPairReveal(firstCardId, secondCardId)
                    }
                }
            }

        }
        //Return to main
        override fun onLongPress() {
            (context as? GameActivity)?.finish()
        }


        override fun onSingleTouch(){}
    }
}
