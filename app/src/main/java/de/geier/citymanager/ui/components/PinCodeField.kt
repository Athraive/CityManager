package de.geier.citymanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    BasicTextField(
        value = value,

        onValueChange = {

            if (
                it.length <= 4 &&
                it.all(Char::isDigit)
            ) {

                onValueChange(it)
            }
        },

        modifier = modifier
            .fillMaxWidth(),

        cursorBrush = SolidColor(
            MaterialTheme.colorScheme.primary
        ),

        decorationBox = {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                repeat(4) { index ->

                    val char =
                        value
                            .getOrNull(index)
                            ?.toString()
                            ?: ""

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .heightIn(min = 64.dp)
                            .clip(
                                RoundedCornerShape(18.dp)
                            )
                            .background(
                                MaterialTheme
                                    .colorScheme
                                    .surfaceVariant
                                    .copy(alpha = 0.92f)
                            )
                            .border(
                                width = 1.dp,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .outline,

                                shape =
                                    RoundedCornerShape(18.dp)
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text = char,

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge,

                            fontSize = 30.sp,

                            color =
                                Color.White.copy(
                                    alpha = 0.92f
                                ),

                            textAlign =
                                TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}