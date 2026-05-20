package de.geier.citymanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.R
import de.geier.citymanager.ui.components.PinCodeField
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory
import androidx.compose.ui.unit.dp

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.start_background
            ),

            contentDescription = null,

            contentScale = ContentScale.Crop,

            modifier = Modifier
                .fillMaxSize()
                .alpha(0.32f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.25f)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement =
                Arrangement.Center,

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    if (showSlInput)
                        "Spielleiter-Code"
                    else
                        "Rolle wählen",

                style =
                    MaterialTheme
                        .typography
                        .headlineLarge,

                color =
                    Color.White.copy(alpha = 0.92f)
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            if (!showSlInput) {

                RoleCard(
                    title = "Spieler",

                    description =
                        "Erkunde Orte und Geschichten.",

                    onClick = {
                        onAccessGranted(Role.PLAYER)
                    }
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                RoleCard(
                    title = "Spielleiter",

                    description =
                        "Verwalte und erweitere die Welt.",

                    onClick = {
                        showSlInput = true
                        errorMessage = null
                        slCodeInput = ""
                    }
                )
            }

            if (showSlInput && city != null) {

                Text(
                    text = "SL-Code eingeben",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    color =
                        Color.White.copy(alpha = 0.92f)
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                PinCodeField(
                    value = slCodeInput,

                    onValueChange = {
                        slCodeInput = it
                        errorMessage = null
                    }
                )

                if (errorMessage != null) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = errorMessage!!,

                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable {
                onClick()
            },

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme
                    .colorScheme
                    .surface
                    .copy(alpha = 0.88f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = title,

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = description,

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}