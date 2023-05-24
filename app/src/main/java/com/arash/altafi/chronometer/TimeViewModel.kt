package com.arash.altafi.chronometer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.arash.altafi.chronometer.State.RUNNING
import com.arash.altafi.chronometer.State.STOPPED
import com.arash.altafi.chronometer.parser.TimeParser
import java.util.Timer
import java.util.TimerTask

class TimeViewModel : ViewModel() {

    val timeText: LiveData<String>
    val timerState: MutableLiveData<State> by lazy { MutableLiveData<State>() }

    private val time: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    private lateinit var timer: Timer
    private var initialTime: Long = 0 * 60 * 1000

    init {
        timeText = Transformations.map(time) { numericTime ->
            TimeParser.parse(numericTime)
        }
        time.value = initialTime
        timerState.value = STOPPED
    }

    fun start() {
        setupWorker()
        timerState.value = RUNNING
    }

    fun stop() {
        tierDownWorker()
        timerState.value = STOPPED
    }

    fun restart() {
        time.value = initialTime
        if (timerState.value == RUNNING) {
            tierDownWorker()
            setupWorker()
        }
    }

    private fun setupWorker() {
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                time.postValue(time.value?.plus(1000))
            }
        }, 0, 1000)
    }

    private fun tierDownWorker() {
        timer.cancel()
    }
}