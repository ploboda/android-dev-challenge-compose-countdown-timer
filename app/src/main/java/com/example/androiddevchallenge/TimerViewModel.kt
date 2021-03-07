/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimerViewModel : ViewModel() {

    private val _counting = MutableLiveData(false)
    val counting: LiveData<Boolean> = _counting

    private val _counterValue = MutableLiveData(Duration.ZERO)
    val counterValue: LiveData<Duration> = _counterValue

    private val _counterProgress = MutableLiveData(0f)
    val counterProgress: LiveData<Float> = _counterProgress

    private val _showCompleteMessage = MutableLiveData(false)
    val showCompleteMessage: LiveData<Boolean> = _showCompleteMessage

    private var timer: CountDownTimer? = null

    fun onStartClick(pickedTime: Duration) {
        _counting.value = true
        val pickedTimeMillis = pickedTime.inMilliseconds
        val pickedTimeSecs = pickedTime.inSeconds.toInt()
        timer = object : CountDownTimer(pickedTimeMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                (millisUntilFinished / 1000f).roundToInt().let { secsUntilFinished ->
                    _counterValue.value = secsUntilFinished.toDuration(DurationUnit.SECONDS)
                    _counterProgress.value =
                        (pickedTimeSecs - secsUntilFinished + 1) / pickedTimeSecs.toFloat()
                }
            }

            override fun onFinish() {
                _counting.value = false
                _showCompleteMessage.value = true
                _counterProgress.value = 0f
            }
        }.start()
    }

    fun onCancelClick() {
        timer?.cancel()
        _counting.value = false
        _counterProgress.value = 0f
    }

    fun onShowCompleteMessageShown() {
        _showCompleteMessage.value = false
    }
}
