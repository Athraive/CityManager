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
fun CreateCityScreen(
    onCityCreated: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    var cityName by remember { mutableStateOf("") }
    var cityCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Neue Stadt anlegen",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Stadtname") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cityCode,
            onValueChange = {
                if (it.length <= 4) cityCode = it
            },
            label = { Text("SL-Code (4-stellig)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {
                    if (cityName.isNotBlank() && cityCode.length == 4) {
                        viewModel.createCity(
                            name = cityName.trim(),
                            gameMasterCode = cityCode
                        ) { newId ->
                            onCityCreated(newId)
                        }
                    }
                }
            ) {
                Text("Erstellen")
            }

            OutlinedButton(onClick = onCancel) {
                Text("Abbrechen")
            }
        }
    }
}
