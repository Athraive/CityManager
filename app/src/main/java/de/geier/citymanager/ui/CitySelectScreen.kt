package de.geier.citymanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import de.geier.citymanager.R
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.ui.theme.AsiaFont
import de.geier.citymanager.ui.theme.CityStylePreset
import de.geier.citymanager.ui.theme.FontPreset
import de.geier.citymanager.ui.theme.MedievalFont
import de.geier.citymanager.ui.theme.ScifiFont
import de.geier.citymanager.ui.theme.WesternFont
import de.geier.citymanager.ui.theme.displayName
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory
import de.geier.citymanager.ui.components.PinCodeField

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
                .padding(16.dp)
        ) {

            Text(
                text = "Stadt auswählen",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
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

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

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
}

@Composable
private fun CityItem(
    city: CityEntity,
    onEnter: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val fontPreset =
        FontPreset.from(city.fontPreset)

    val stylePreset =
        CityStylePreset.from(city.stylePreset)

    val headlineFont = when (fontPreset) {

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

    val headlineSpacing = when (stylePreset) {

        CityStylePreset.NEON_MATRIX -> 2.sp

        CityStylePreset.URBAN_GREY -> 1.5.sp

        CityStylePreset.DUST -> 0.8.sp

        CityStylePreset.FILM_NOIR -> 0.2.sp

        CityStylePreset.PARCHEMENT -> 0.3.sp

        CityStylePreset.BLOSSOM -> 0.sp
    }

    val headlineScale = when (fontPreset) {

        FontPreset.DEFAULT -> 1.0f

        FontPreset.SCIFI -> 1.0f

        FontPreset.WESTERN -> 1.03f

        FontPreset.ASIA -> 1.08f

        FontPreset.MEDIEVAL -> 1.15f
    }

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
        modifier = Modifier
            .fillMaxWidth(),

        onClick = {
            onEnter()
        },

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.88f
                )
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),

                contentAlignment = Alignment.Center
            ) {

                if (city.coatOfArmsUri != null) {

                    AsyncImage(
                        model = city.coatOfArmsUri,
                        contentDescription = "Wappen",

                        modifier = Modifier.fillMaxSize(),

                        contentScale = ContentScale.Fit
                    )

                } else {

                    Icon(
                        imageVector =
                            Icons.Default.LocationCity,

                        contentDescription = null,

                        modifier = Modifier.size(40.dp),

                        tint =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(18.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = city.name,

                    style =
                        MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = headlineFont,
                            letterSpacing = headlineSpacing,

                            fontSize =
                                MaterialTheme.typography
                                    .headlineSmall
                                    .fontSize * headlineScale
                        ),

                    color =
                        MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        "${stylePreset.displayName()} • " +
                                fontPreset.displayName(),

                    style = MaterialTheme.typography.bodyMedium,

                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                HorizontalDivider(
                    color =
                        MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.End
                ) {

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

                    PinCodeField(
                        value = enteredCode,

                        onValueChange = {
                            enteredCode = it
                            codeError = false
                        }
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
                            enteredCode = ""
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

                    PinCodeField(
                        value = enteredCode,

                        onValueChange = {
                            enteredCode = it
                            codeError = false
                        }
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
                            enteredCode = ""
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