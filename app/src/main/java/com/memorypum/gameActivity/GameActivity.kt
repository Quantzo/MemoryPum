package com.memorypum.gameActivity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.AppCompatTextView
import android.view.MotionEvent
import com.memorypum.R
import com.memorypum.common.AppGesturesListener


class GameActivity : AppCompatActivity() {

    private var gameActivityGestureListener: GestureDetectorCompat? = null

    var game: Game? = null
        get
        private set

    private var points: Int = 0

    var textView: AppCompatTextView? = null
        private set
        get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val numberOfPairs = intent.getIntExtra("Number_Of_Pairs", 8)
        gameActivityGestureListener = GestureDetectorCompat(this, GameActivityGestures(this))
        game = Game(numberOfPairs)

        textView = findViewById(R.id.textGame) as AppCompatTextView
        textView?.text = game?.getCurrentIdIfRevealed().toString()


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

    class GameActivityGestures(context: AppCompatActivity) : AppGesturesListener(context)
    {
        //Move Right
        override fun onSwipeLeft() {
            val result = (context as GameActivity).game?.move({ p, mF -> p + 1 <= mF }, { p, r, m -> Math.min(m, (p * r) + 4) }, { p -> p + 1 }) ?: false

            if (!result) {
                //TODO("Signal that move was unsuccessful")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }


        }
        //Move Left
        override fun onSwipeRight() {
            val result = (context as GameActivity).game?.move({ p, mF -> p - 1 >= mF }, { p, r, m -> r * 5 }, { p -> p - 1 }) ?: false
            if (!result) {
                //TODO("Signal that move was unsuccessful")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }
        }
        //Move Down
        override fun onSwipeUp() {
            val result = (context as GameActivity).game?.move({ p, mF -> p + 5 <= mF }, { p, r, m -> m }, { p -> p + 5 }) ?: false
            if (!result) {
                //TODO("Signal that move was unsuccessful")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }
        }
        //Move Up
        override fun onSwipeDown() {
            val result = (context as GameActivity).game?.move({ p, mF -> p - 5 >= mF }, { p, r, m -> 0 }, { p -> p - 5 }) ?: false
            if (!result) {
                //TODO("Signal that move was unsuccessful")
            } else {
                context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
            }
        }
        //Reveal
        override fun onDoubleTouch() {
            val result = (context as GameActivity).game?.canReveal() ?: false
            if (!result) {
                //TODO("Signal that this card was already Revealed")
            } else {
                val (firstCard, firstCardId, secondCardId, resultOfSecondCardReveal) = context.game?.reveal() ?: Game.RevealResult(false, 0, 0, false)
                if (firstCard) {
                    context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
                    //TODO("Signal that first Card was Reveled")
                } else {
                    if (resultOfSecondCardReveal) {
                        context.textView?.text = context.game?.getCurrentIdIfRevealed().toString()
                        context.points += 1
                        //if(context.game?.isGameOver() ?: false)
                        //TODO("Signal that the game is over")
                        //TODO("Signal that the Pair was Guessed")

                    } else {
                        //TODO("Signal that the Pair was not Guessed")
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
