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

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.time.Duration

@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {

    val counting: Boolean by viewModel.counting.observeAsState(false)
    val counterValue: Duration by viewModel.counterValue.observeAsState(Duration.ZERO)
    val counterProgress: Float by viewModel.counterProgress.observeAsState(0f)
    val showCompleteMessage: Boolean by viewModel.showCompleteMessage.observeAsState(false)

    if (showCompleteMessage) {
        Toast.makeText(
            LocalContext.current,
            stringResource(R.string.complete_message),
            Toast.LENGTH_LONG
        ).show()
        viewModel.onShowCompleteMessageShown()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var pickedTime by remember { mutableStateOf(Duration.ZERO) }

            TimePicker(
                modifier = Modifier.padding(top = 32.dp),
                enabled = !counting
            ) { pickedTime = it }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                shape = RoundedCornerShape(8.dp),
                enabled = !counting,
                onClick = { viewModel.onStartClick(pickedTime) }
            ) {
                Text(text = stringResource(R.string.button_start))
            }
            if (counting) {
                Timer(
                    modifier = Modifier.weight(1f),
                    value = counterValue,
                    progress = counterProgress
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    shape = RoundedCornerShape(8.dp),
                    onClick = viewModel::onCancelClick
                ) {
                    Text(text = stringResource(R.string.button_cancel))
                }
            }
        }
    }
}
