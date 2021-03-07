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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun TimePicker(modifier: Modifier, enabled: Boolean, onValueChange: (Duration) -> Unit) {
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    fun changed() {
        onValueChange(
            hours.toDuration(DurationUnit.HOURS) + minutes.toDuration(DurationUnit.MINUTES) + seconds.toDuration(
                DurationUnit.SECONDS
            )
        )
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IntPicker(
            value = hours,
            range = 0..99,
            label = stringResource(R.string.hours),
            enabled = enabled
        ) {
            hours = it
            changed()
        }
        Text(
            textAlign = TextAlign.Center,
            text = ":",
            modifier = Modifier.padding(12.dp)
        )
        IntPicker(
            value = minutes,
            range = 0..59,
            label = stringResource(R.string.minutes),
            enabled = enabled
        ) {
            minutes = it
            changed()
        }
        Text(
            textAlign = TextAlign.Center,
            text = ":",
            modifier = Modifier.padding(12.dp)
        )
        IntPicker(
            value = seconds, range = 0..59,
            label = stringResource(R.string.seconds),
            enabled = enabled
        ) {
            seconds = it
            changed()
        }
    }
}

@Composable
fun IntPicker(
    value: Int,
    range: IntRange,
    label: String,
    enabled: Boolean,
    onValueChange: (Int) -> Unit
) {
    var textFieldRange by remember {
        mutableStateOf(TextRange(value.toString().length))
    }
    val focusManager = LocalFocusManager.current
    TextField(
        value = TextFieldValue(value.toString(), textFieldRange),
        onValueChange = { newValue ->
            try {
                newValue.text.toInt().let {
                    if (it in range) {
                        onValueChange(it)
                        textFieldRange = newValue.selection
                    }
                }
            } catch (e: NumberFormatException) {
                onValueChange(0)
                textFieldRange = TextRange(1)
            }
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        visualTransformation = { text ->
            if (text.text.length == 1) {
                TransformedText(
                    AnnotatedString("0${text.text}"),
                    offsetMapping = object : OffsetMapping {
                        override fun originalToTransformed(offset: Int) = offset + 1
                        override fun transformedToOriginal(offset: Int) = offset - 1
                    }
                )
            } else {
                TransformedText(text, OffsetMapping.Identity)
            }
        },
        modifier = Modifier.requiredWidth(90.dp),
        enabled = enabled,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}
