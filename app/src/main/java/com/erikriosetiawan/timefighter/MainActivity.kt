package com.erikriosetiawan.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvScore: TextView
    private lateinit var tvTimeRemaining: TextView
    private lateinit var btnTapMe: Button
    private lateinit var counDownTimer: CountDownTimer

    private var score = 0
    private var initialCountDown = 60000L
    private var countDownInteval = 1000L
    private var timeLeftOnTimer = 6000L

    private var gameStarted = false

    private val TAG = MainActivity::class.java.simpleName

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    private lateinit var animButton: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is $score")

        tvScore = findViewById(R.id.tv_score)
        tvTimeRemaining = findViewById(R.id.tv_time_remaining)
        btnTapMe = findViewById(R.id.btn_tap_me)

        resetGame()

        animButton = AnimationUtils.loadAnimation(this, R.anim.bounce)

        btnTapMe.setOnClickListener(this)

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_tap_me -> {
                incrementScore()
                btnTapMe.startAnimation(animButton)
            }
        }
    }

    private fun restoreGame() {
        tvScore.text = getString(R.string.your_score, score.toString())
        val restoredTime = timeLeftOnTimer / 1000
        tvTimeRemaining.text = getString(R.string.time_remaining, restoredTime.toString())

        counDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInteval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                var timeLeft = millisUntilFinished / 1000
                tvTimeRemaining.text = getString(R.string.time_remaining, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }

        counDownTimer.start()
        gameStarted = true
    }

    private fun resetGame() {
        score = 0
        tvScore.text = getString(R.string.your_score, score.toString())
        val initialTimeLeft = initialCountDown / 1000
        tvTimeRemaining.text = getString(R.string.time_remaining, initialTimeLeft.toString())

        counDownTimer = object : CountDownTimer(initialCountDown, countDownInteval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeRemaining = millisUntilFinished / 1000
                tvTimeRemaining.text = getString(R.string.time_remaining, timeRemaining.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }

    private fun startGame() {
        counDownTimer.start()
        gameStarted = true
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score++
        tvScore.text = getString(R.string.your_score, score.toString())
        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        tvScore.startAnimation(blinkAnimation)
    }

    private fun endGame() {
        Toast.makeText(
            this,
            getString(R.string.game_over_message, tvScore.text.toString().trim()),
            Toast.LENGTH_SHORT
        ).show()
        resetGame()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        counDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_about) {
            showInfo()
        }
        return true
    }

    private fun showInfo() {
        val dialogTile = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTile)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }
}