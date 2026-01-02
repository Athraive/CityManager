package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameMasterCodeScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {

    var code by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Spielleiter-Code",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = code,
            onValueChange = {
                code = it
                error = false
            },
            label = { Text("Code eingeben") },
            isError = error,
            modifier = Modifier.fillMaxWidth()
        )

        if (error) {
            Text(
                text = "Falscher Code",
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (code == "ELDORIA") {
                    onSuccess()
                } else {
                    error = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Bestätigen", fontSize = 18.sp)
        }
    }
}

