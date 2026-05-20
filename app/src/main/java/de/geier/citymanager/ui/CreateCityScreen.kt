package de.geier.citymanager.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.geier.citymanager.R
import de.geier.citymanager.data.entity.CityEntity
import de.geier.citymanager.ui.background.CityThemePresets
import de.geier.citymanager.ui.components.PinCodeField
import de.geier.citymanager.ui.theme.AsiaFont
import de.geier.citymanager.ui.theme.CityStylePreset
import de.geier.citymanager.ui.theme.FontPreset
import de.geier.citymanager.ui.theme.MedievalFont
import de.geier.citymanager.ui.theme.ScifiFont
import de.geier.citymanager.ui.theme.WesternFont
import de.geier.citymanager.ui.theme.displayName
import de.geier.citymanager.ui.viewmodel.CitySelectViewModel
import de.geier.citymanager.ui.viewmodel.CitySelectViewModelFactory
import androidx.compose.foundation.BorderStroke
import de.geier.citymanager.ui.theme.CityTheme
import de.geier.citymanager.ui.theme.toColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCityScreen(
    onCityCreated: (String) -> Unit,
    onCancel: () -> Unit,

    existingCity: CityEntity? = null,

    onCityUpdated: (() -> Unit)? = null
) {

    val context = LocalContext.current

    val viewModel: CitySelectViewModel = viewModel(
        factory = CitySelectViewModelFactory(context)
    )

    var cityName by remember(existingCity) {
        mutableStateOf(existingCity?.name ?: "")
    }

    var cityCode by remember(existingCity) {
        mutableStateOf(existingCity?.gameMasterCode ?: "")
    }

    var selectedTheme by remember(existingCity) {
        mutableStateOf(existingCity?.fontPreset ?: "SCIFI")
    }

    var selectedStylePreset by remember(existingCity) {
        mutableStateOf(
            existingCity?.stylePreset
                ?: CityStylePreset.URBAN_GREY.name
        )
    }


    val isNameValid = cityName.isNotBlank()
    val isCodeValid = cityCode.length == 4

    val fontPreset =
        FontPreset.from(selectedTheme)

    val stylePreset =
        CityStylePreset.from(selectedStylePreset)

    val previewFont = when (fontPreset) {

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

    val previewScale = when (fontPreset) {

        FontPreset.DEFAULT -> 1.0f

        FontPreset.SCIFI -> 1.0f

        FontPreset.WESTERN -> 1.04f

        FontPreset.ASIA -> 1.08f

        FontPreset.MEDIEVAL -> 1.15f
    }
    val previewColorScheme = CityTheme(
        stylePreset = stylePreset,
        backgroundImageUri = null,
        fontPreset = fontPreset
    ).toColorScheme()


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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),

            verticalArrangement =
                Arrangement.spacedBy(18.dp)
        ) {

            Text(
                text =
                    if (existingCity == null)
                        "Neue Stadt"
                    else
                        "Stadt bearbeiten",

                style =
                    MaterialTheme
                        .typography
                        .headlineLarge,

                color =
                    Color.White.copy(alpha = 0.92f)
            )

            /* =====================================================
             * LIVE PREVIEW
             * ===================================================== */

            Card(
                colors = CardDefaults.cardColors(
                    containerColor =
                        previewColorScheme.primary.copy(
                            alpha = 0.92f
                        )
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Text(
                        text =
                            if (cityName.isBlank())
                                "Deine Stadt"
                            else
                                cityName,

                        style =
                            MaterialTheme
                                .typography
                                .headlineLarge
                                .copy(
                                    fontFamily = previewFont,

                                    fontSize =
                                        MaterialTheme
                                            .typography
                                            .headlineLarge
                                            .fontSize * previewScale
                                ),

                        color =
                            previewColorScheme.onPrimary
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "${stylePreset.displayName()} • " +
                                    fontPreset.displayName(),

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        color =
                            previewColorScheme
                                .onPrimary
                    )
                }
            }

            /* =====================================================
             * IDENTITÄT
             * ===================================================== */

            Card(
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
                        .fillMaxWidth()
                        .padding(20.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Identität",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurface
                    )

                    OutlinedTextField(
                        value = cityName,

                        onValueChange = {
                            cityName = it
                        },

                        label = {
                            Text("Stadtname")
                        },

                        isError = !isNameValid,

                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!isNameValid) {

                        Text(
                            text =
                                "Bitte Stadtname eingeben",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }

                    Text(
                        text = "Spielleiter-Code",

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurface
                    )

                    PinCodeField(
                        value = cityCode,

                        onValueChange = {
                            cityCode = it
                        }
                    )

                    if (!isCodeValid) {

                        Text(
                            text =
                                "Code muss 4-stellig sein",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }
                }
            }

            /* =====================================================
 * SCHRIFTSTIL
 * ===================================================== */

            Card(
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
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        text = "Schriftstil",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    FontStyleCard(
                        title = "Future World",

                        description =
                            "Futuristisch • Technologisch • Kühl",

                        fontFamily = ScifiFont,

                        selected =
                            selectedTheme ==
                                    FontPreset.SCIFI.name,

                        onClick = {
                            selectedTheme =
                                FontPreset.SCIFI.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    FontStyleCard(
                        title = "Old Kingdom",

                        description =
                            "Majestätisch • Historisch • Mystisch",

                        fontFamily = MedievalFont,

                        selected =
                            selectedTheme ==
                                    FontPreset.MEDIEVAL.name,

                        onClick = {
                            selectedTheme =
                                FontPreset.MEDIEVAL.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    FontStyleCard(
                        title = "Western Frontier",

                        description =
                            "Rustikal • Frei • Grenzland",

                        fontFamily = WesternFont,

                        selected =
                            selectedTheme ==
                                    FontPreset.WESTERN.name,

                        onClick = {
                            selectedTheme =
                                FontPreset.WESTERN.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    FontStyleCard(
                        title = "Jade Empire",

                        description =
                            "Elegant • Harmonisch • Fernöstlich",

                        fontFamily = AsiaFont,

                        selected =
                            selectedTheme ==
                                    FontPreset.ASIA.name,

                        onClick = {
                            selectedTheme =
                                FontPreset.ASIA.name
                        }
                    )
                }
            }

            /* =====================================================
 * FARBSCHEMA
 * ===================================================== */

            Card(
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
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        text = "Farbschema",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    StylePresetCard(
                        title = "Urban Grey",

                        description =
                            "Modern • Kühl • Urban",

                        stylePreset =
                            CityStylePreset.URBAN_GREY,

                        selected =
                            selectedStylePreset ==
                                    CityStylePreset.URBAN_GREY.name,

                        onClick = {
                            selectedStylePreset =
                                CityStylePreset.URBAN_GREY.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    StylePresetCard(
                        title = "Worn Parchment",

                        description =
                            "Alt • Warm • Mystisch",


                        stylePreset =
                            CityStylePreset.PARCHEMENT,

                        selected =
                            selectedStylePreset ==
                                    CityStylePreset.PARCHEMENT.name,

                        onClick = {
                            selectedStylePreset =
                                CityStylePreset.PARCHEMENT.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    StylePresetCard(
                        title = "Cherry Blossom",

                        description =
                            "Elegant • Harmonisch • Ruhig",

                        stylePreset =
                            CityStylePreset.BLOSSOM,

                        selected =
                            selectedStylePreset ==
                                    CityStylePreset.BLOSSOM.name,

                        onClick = {
                            selectedStylePreset =
                                CityStylePreset.BLOSSOM.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    StylePresetCard(
                        title = "Dusty Road",

                        description =
                            "Staubig • Frei • Grenzland",

                        stylePreset =
                            CityStylePreset.DUST,

                        selected =
                            selectedStylePreset ==
                                    CityStylePreset.DUST.name,

                        onClick = {
                            selectedStylePreset =
                                CityStylePreset.DUST.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    StylePresetCard(
                        title = "Film Noir",

                        description =
                            "Dunkel • Elegant • Kontrastreich",

                        stylePreset =
                            CityStylePreset.FILM_NOIR,

                        selected =
                            selectedStylePreset ==
                                    CityStylePreset.FILM_NOIR.name,

                        onClick = {
                            selectedStylePreset =
                                CityStylePreset.FILM_NOIR.name
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    StylePresetCard(
                        title = "Neon Matrix",

                        description =
                            "Cyberpunk • Leuchtend • Digital",

                        stylePreset =
                            CityStylePreset.NEON_MATRIX,

                        selected =
                            selectedStylePreset ==
                                    CityStylePreset.NEON_MATRIX.name,

                        onClick = {
                            selectedStylePreset =
                                CityStylePreset.NEON_MATRIX.name
                        }
                    )
                }
            }

            /* =====================================================
             * BUTTONS
             * ===================================================== */

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Button(

                    onClick = {

                        if (existingCity == null) {

                            viewModel.createCity(
                                name =
                                    cityName.trim(),

                                gameMasterCode =
                                    cityCode,

                                backgroundPreset =
                                    "WHITE",

                                fontPreset =
                                    selectedTheme,

                                stylePreset =
                                    selectedStylePreset

                            ) { newId ->

                                onCityCreated(newId)
                            }

                        } else {

                            viewModel.saveCity(
                                existingCity.copy(
                                    name =
                                        cityName.trim(),

                                    gameMasterCode =
                                        cityCode,

                                    fontPreset =
                                        selectedTheme,

                                    stylePreset =
                                        selectedStylePreset
                                )
                            )

                            onCityUpdated?.invoke()
                        }
                    },

                    enabled =
                        isNameValid &&
                                isCodeValid
                ) {

                    Text(
                        if (existingCity == null)
                            "Erstellen"
                        else
                            "Speichern"
                    )
                }

                OutlinedButton(
                    onClick = onCancel
                ) {

                    Text("Abbrechen")
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )
        }
    }
}
@Composable
private fun FontStyleCard(
    title: String,
    description: String,
    fontFamily: FontFamily,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),

        onClick = onClick,

        colors = CardDefaults.cardColors(

            containerColor =

                if (selected)

                    MaterialTheme
                        .colorScheme
                        .primaryContainer

                else

                    MaterialTheme
                        .colorScheme
                        .surfaceVariant
                        .copy(alpha = 0.65f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Text(
                text = title,

                fontFamily = fontFamily,

                style =
                    MaterialTheme
                        .typography
                        .headlineSmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )

            Spacer(
                modifier = Modifier.height(8.dp)
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
@Composable
private fun StylePresetCard(
    title: String,
    description: String,
    stylePreset: CityStylePreset,
    selected: Boolean,
    onClick: () -> Unit
) {

    val colorScheme = CityTheme(
        stylePreset = stylePreset,
        backgroundImageUri = null,
        fontPreset = FontPreset.SCIFI
    ).toColorScheme()


    val textColor =
        Color.White.copy(alpha = 0.92f)


    Card(
        modifier = Modifier
            .fillMaxWidth(),

        onClick = onClick,

        colors = CardDefaults.cardColors(

            containerColor =

                if (selected)

                    colorScheme.primary

                else

                    colorScheme.primary.copy(
                        alpha = 0.72f
                    )
        ),

        border =

            if (selected)

                BorderStroke(
                    width = 2.dp,
                    color = textColor
                )

            else

                null
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Text(
                text = title,

                style =
                    MaterialTheme
                        .typography
                        .headlineSmall,

                color = textColor
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = description,

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,

                color =
                    textColor.copy(alpha = 0.78f)
            )
        }
    }
}