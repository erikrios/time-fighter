package com.erikriosetiawan.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvScore: TextView
    private lateinit var tvTimeRemaining: TextView
    private lateinit var btnTapMe: Button
    private lateinit var counDownTimer: CountDownTimer

    private var score = 0
    private var initialCountDown = 60000L
    private var countDownInteval = 1000L

    private var gameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvScore = findViewById(R.id.tv_score)
        tvTimeRemaining = findViewById(R.id.tv_time_remaining)
        btnTapMe = findViewById(R.id.btn_tap_me)

        resetGame()

        btnTapMe.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_tap_me -> {
                incrementScore()
            }
        }
    }

    private fun resetGame() {
        score = 0
        tvScore.text = getString(R.string.your_score, score.toString())
        val initialTimeLeft = initialCountDown / 1000
        tvTimeRemaining.text = getString(R.string.time_remaining, initialTimeLeft.toString())

        counDownTimer = object : CountDownTimer(initialCountDown, countDownInteval) {
            override fun onTick(millisUntilFinished: Long) {
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
    }

    private fun endGame() {
        Toast.makeText(
            this,
            getString(R.string.game_over_message, tvScore.text.toString().trim()),
            Toast.LENGTH_SHORT
        ).show()
        resetGame()
    }
}