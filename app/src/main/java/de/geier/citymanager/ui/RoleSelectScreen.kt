package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
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

    var slCodeInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSlInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Rolle wählen",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = {
            onAccessGranted(Role.PLAYER)
        }) {
            Text("Spieler")
        }

        Button(onClick = {
            showSlInput = true
            errorMessage = null
        }) {
            Text("Spielleiter")
        }

        if (showSlInput && city != null) {

            OutlinedTextField(
                value = slCodeInput,
                onValueChange = {
                    if (it.length <= 4) slCodeInput = it
                },
                label = { Text("SL-Code") },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (slCodeInput == city.gameMasterCode) {
                        onAccessGranted(Role.GAME_MASTER)
                    } else {
                        errorMessage = "Falscher Code"
                    }
                }
            ) {
                Text("Bestätigen")
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
