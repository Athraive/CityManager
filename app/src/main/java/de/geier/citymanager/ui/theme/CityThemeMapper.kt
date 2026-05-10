package de.geier.citymanager.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import de.geier.citymanager.data.entity.CityEntity

/* ---------------------------------------------------
 * DB → Theme
 * --------------------------------------------------- */
fun CityEntity.toCityTheme(): CityTheme =
    CityTheme(
        stylePreset = when (stylePreset) {
            "SCIFI" -> CityStylePreset.URBAN_GREY
            "FANTASY" -> CityStylePreset.PARCHEMENT
            "ASIA" -> CityStylePreset.BLOSSOM
            "WESTERN" -> CityStylePreset.DUST
            else -> CityStylePreset.from(stylePreset)
        },
        backgroundImageUri = backgroundImageUri,
        fontPreset = FontPreset.from(fontPreset)
    )

/* ---------------------------------------------------
 * Theme → Colors
 * --------------------------------------------------- */
fun CityTheme.toColorScheme(): ColorScheme {
    return when (stylePreset) {

        CityStylePreset.URBAN_GREY -> darkColorScheme(
            primary = Color(0xFF8FA3B0),
            secondary = Color(0xFF5F6F7A),
            tertiary = Color(0xFFB0C4D4),
            background = Color(0xFF0F1418),
            surface = Color(0xFF1A2228),
            onPrimary = Color.Black,
            onBackground = Color(0xFFE6EEF3),
            onSurface = Color(0xFFE6EEF3)
        )

        CityStylePreset.PARCHEMENT -> darkColorScheme(
            primary = Color(0xFFD6C5A3),
            secondary = Color(0xFFA89F91),
            tertiary = Color(0xFFE8DFC8),
            background = Color(0xFF1B1A17),
            surface = Color(0xFF2A2823),
            onPrimary = Color.Black,
            onBackground = Color(0xFFF2EDE3),
            onSurface = Color(0xFFF2EDE3)
        )

        CityStylePreset.BLOSSOM -> darkColorScheme(
            primary = Color(0xFFBFA2DB),
            secondary = Color(0xFF8C6FA5),
            tertiary = Color(0xFFE8D8F5),
            background = Color(0xFF141018),
            surface = Color(0xFF201A26),
            onPrimary = Color.Black,
            onBackground = Color(0xFFF1E9F7),
            onSurface = Color(0xFFF1E9F7)
        )

        CityStylePreset.DUST -> darkColorScheme(
            primary = Color(0xFFC2A27A),
            secondary = Color(0xFF8E6E4A),
            tertiary = Color(0xFFE0C7A2),
            background = Color(0xFF1A1410),
            surface = Color(0xFF2A211A),
            onPrimary = Color.Black,
            onBackground = Color(0xFFF3E7D8),
            onSurface = Color(0xFFF3E7D8)
        )

        CityStylePreset.FILM_NOIR -> darkColorScheme(
            primary = Color(0xFFE0E0E0),
            secondary = Color(0xFFB0B0B0),
            tertiary = Color(0xFFF5F5F5),

            background = Color(0xFF050505),
            surface = Color(0xFF111111),

            onPrimary = Color.Black,
            onBackground = Color(0xFFF2F2F2),
            onSurface = Color(0xFFF2F2F2)
        )

        CityStylePreset.NEON_MATRIX -> darkColorScheme(
            primary = Color(0xFF00FF66),
            secondary = Color(0xFF00CC55),
            tertiary = Color(0xFF88FFAA),

            background = Color(0xFF020402),
            surface = Color(0xFF0A120A),

            onPrimary = Color.Black,
            onBackground = Color(0xFFB8FFC8),
            onSurface = Color(0xFFB8FFC8)
        )
    }
}