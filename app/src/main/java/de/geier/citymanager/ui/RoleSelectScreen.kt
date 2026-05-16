package de.geier.citymanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.theme.AsiaFont
import de.geier.citymanager.ui.theme.FontPreset
import de.geier.citymanager.ui.theme.MedievalFont
import de.geier.citymanager.ui.theme.ScifiFont
import de.geier.citymanager.ui.theme.WesternFont
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@Composable
fun RoleSelectScreen(
    cityId: String,
    onAccessGranted: (Role) -> Unit
) {

    val context = LocalContext.current

    val cityViewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    val cities by cityViewModel.cities.collectAsState()

    val city = cities.find { it.id == cityId }

    val fontPreset =
        FontPreset.from(city?.fontPreset)

    val pinFont = when (fontPreset) {

        FontPreset.DEFAULT ->
            FontFamily.Default

        FontPreset.SCIFI ->
            ScifiFont

        FontPreset.WESTERN ->
            WesternFont

        FontPreset.ASIA ->
            AsiaFont

        FontPreset.MEDIEVAL ->
            MedievalFont
    }

    var slCodeInput by rememberSaveable {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var showSlInput by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(slCodeInput) {

        if (
            city != null &&
            slCodeInput.length == 4
        ) {

            if (slCodeInput == city.gameMasterCode) {

                onAccessGranted(Role.GAME_MASTER)

            } else {

                errorMessage = "Falscher Code"
                slCodeInput = ""
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),

        verticalArrangement =
            Arrangement.spacedBy(
                24.dp,
                Alignment.CenterVertically
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Rolle wählen",
            style =
                MaterialTheme.typography.headlineMedium
        )

        if (!showSlInput) {

            Button(
                onClick = {
                    onAccessGranted(Role.PLAYER)
                }
            ) {
                Text("Spieler")
            }

            Button(
                onClick = {
                    showSlInput = true
                    errorMessage = null
                    slCodeInput = ""
                }
            ) {
                Text("Spielleiter")
            }
        }

        if (showSlInput && city != null) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "SL-Code eingeben",
                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            BasicTextField(
                value = slCodeInput,

                onValueChange = {

                    if (
                        it.length <= 4 &&
                        it.all(Char::isDigit)
                    ) {

                        slCodeInput = it
                        errorMessage = null
                    }
                },

                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                ),

                decorationBox = {

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        repeat(4) { index ->

                            val char =
                                slCodeInput
                                    .getOrNull(index)
                                    ?.toString()
                                    ?: ""

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(
                                        RoundedCornerShape(16.dp)
                                    )
                                    .background(
                                        MaterialTheme
                                            .colorScheme
                                            .surfaceVariant
                                    )
                                    .border(
                                        width = 1.dp,

                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .outline,

                                        shape =
                                            RoundedCornerShape(16.dp)
                                    ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text = char,

                                    fontFamily = pinFont,

                                    fontSize = 28.sp,

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

            if (errorMessage != null) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = errorMessage!!,

                    color =
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}