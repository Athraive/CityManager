package de.geier.citymanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory

@Composable
fun CitySelectScreen(
    onCitySelected: (String) -> Unit,
    onCreateCity: () -> Unit,
    onEditCity: (String) -> Unit
) {

    val context = LocalContext.current

    val viewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    val cities by viewModel.cities.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Stadt auswählen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(cities) { city ->

                CityItem(
                    city = city,

                    onEnter = {
                        onCitySelected(city.id)
                    },

                    onEdit = {
                        onEditCity(city.id)
                    },

                    onDelete = {
                        viewModel.deleteCity(city.id)
                    }
                )
            }

            item {

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onCreateCity,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("➕ Neue Stadt anlegen")
                }
            }
        }
    }
}

@Composable
private fun CityItem(
    city: CityEntity,
    onEnter: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var enteredCode by remember {
        mutableStateOf("")
    }

    var codeError by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                TextButton(
                    onClick = onEnter
                ) {
                    Text("Betreten")
                }

                TextButton(
                    onClick = {
                        enteredCode = ""
                        codeError = false
                        showEditDialog = true
                    }
                ) {
                    Text("Bearbeiten")
                }

                TextButton(
                    onClick = {
                        enteredCode = ""
                        codeError = false
                        showDeleteDialog = true
                    }
                ) {
                    Text("Löschen")
                }
            }
        }
    }

    /* ---------------------------------------------------
     * EDIT DIALOG
     * --------------------------------------------------- */

    if (showEditDialog) {

        AlertDialog(

            onDismissRequest = {
                showEditDialog = false
            },

            title = {
                Text("Stadt bearbeiten")
            },

            text = {

                Column {

                    Text(
                        "Bitte SL-Code eingeben."
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = enteredCode,

                        onValueChange = {
                            enteredCode = it
                            codeError = false
                        },

                        label = {
                            Text("SL-Code")
                        },

                        isError = codeError,

                        singleLine = true
                    )

                    if (codeError) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Falscher Code",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        if (enteredCode == city.gameMasterCode) {

                            showEditDialog = false
                            onEdit()

                        } else {

                            codeError = true
                        }
                    }
                ) {
                    Text("Bestätigen")
                }
            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        showEditDialog = false
                    }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }

    /* ---------------------------------------------------
     * DELETE DIALOG
     * --------------------------------------------------- */

    if (showDeleteDialog) {

        AlertDialog(

            onDismissRequest = {
                showDeleteDialog = false
            },

            title = {
                Text("Stadt löschen")
            },

            text = {

                Column {

                    Text(
                        "Diese Aktion kann nicht rückgängig gemacht werden."
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        "Bitte SL-Code eingeben, um die Stadt zu löschen."
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = enteredCode,

                        onValueChange = {
                            enteredCode = it
                            codeError = false
                        },

                        label = {
                            Text("SL-Code")
                        },

                        isError = codeError,

                        singleLine = true
                    )

                    if (codeError) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Falscher Code",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },

            confirmButton = {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.error
                    ),

                    onClick = {

                        if (enteredCode == city.gameMasterCode) {

                            showDeleteDialog = false
                            onDelete()

                        } else {

                            codeError = true
                        }
                    }
                ) {
                    Text("Löschen")
                }
            },

            dismissButton = {

                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Abbrechen")
                }
            }
        )
    }
}