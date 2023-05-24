package com.arash.altafi.chronometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arash.altafi.chronometer.State.RUNNING
import com.arash.altafi.chronometer.State.STOPPED
import com.arash.altafi.chronometer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var model: TimeViewModel
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        model = ViewModelProvider(this)[TimeViewModel::class.java]

        setupButtons()
        setupTimeObserver()
        setupStateObserver()
    }

    private fun setupButtons() = binding.apply {
        btnStart.setOnClickListener { model.start() }
        btnStop.setOnClickListener { model.stop() }
        btnRestart.setOnClickListener { model.restart() }
    }

    private fun setupTimeObserver() = binding.apply {
        val timeObserver = Observer<String> { time ->
            tvTime.text = time
        }
        model.timeText.observe(this@MainActivity, timeObserver)
    }

    private fun setupStateObserver() = binding.apply {
        val stateObserver = Observer<State> { state ->
            btnStart.isEnabled = state == STOPPED
            btnStart.isClickable = state == STOPPED
            btnStop.isEnabled = state == RUNNING
            btnStop.isClickable = state == RUNNING
        }
        model.timerState.observe(this@MainActivity, stateObserver)
    }
}
