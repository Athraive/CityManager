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

        /* =====================================================
         * URBAN GREY
         * ===================================================== */

        CityStylePreset.URBAN_GREY -> darkColorScheme(

            background = Color(0xFF111417),
            surface = Color(0xFF1C2328),

            primary = Color(0xFF7F919C),
            secondary = Color(0xFF56636B),
            tertiary = Color(0xFFA8B8C2),

            onPrimary = Color.Black,

            onBackground = Color(0xFFF0F2F3),
            onSurface = Color(0xFFF0F2F3)
        )

        /* =====================================================
         * WORN PARCHMENT
         * ===================================================== */

        CityStylePreset.PARCHEMENT -> darkColorScheme(

            background = Color(0xFF1E1A16),
            surface = Color(0xFF322920),

            primary = Color(0xFFD7C19A),
            secondary = Color(0xFFA8906D),
            tertiary = Color(0xFFE9D9BF),

            onPrimary = Color.Black,

            onBackground = Color(0xFFF5EBDD),
            onSurface = Color(0xFFF5EBDD)
        )

        /* =====================================================
         * CHERRY BLOSSOM
         * ===================================================== */

        CityStylePreset.BLOSSOM -> darkColorScheme(

            background = Color(0xFF181218),
            surface = Color(0xFF2A1F2A),

            primary = Color(0xFFD7B6C9),
            secondary = Color(0xFFA98099),
            tertiary = Color(0xFFF0D9E4),

            onPrimary = Color.Black,

            onBackground = Color(0xFFFFEEF5),
            onSurface = Color(0xFFFFEEF5)
        )

        /* =====================================================
         * DUSTY ROAD
         * ===================================================== */

        CityStylePreset.DUST -> darkColorScheme(

            background = Color(0xFF1B140F),
            surface = Color(0xFF30231A),

            primary = Color(0xFFC8A67A),
            secondary = Color(0xFF8C6947),
            tertiary = Color(0xFFE3C49D),

            onPrimary = Color.Black,

            onBackground = Color(0xFFF6E7D5),
            onSurface = Color(0xFFF6E7D5)
        )

        /* =====================================================
         * FILM NOIR
         * ===================================================== */

        CityStylePreset.FILM_NOIR -> darkColorScheme(

            background = Color(0xFF050505),
            surface = Color(0xFF101010),

            primary = Color(0xFFD0D0D0),
            secondary = Color(0xFF808080),
            tertiary = Color(0xFFF0F0F0),

            onPrimary = Color.Black,

            onBackground = Color(0xFFF5F5F5),
            onSurface = Color(0xFFF5F5F5)
        )

        /* =====================================================
         * NEON MATRIX
         * ===================================================== */

        CityStylePreset.NEON_MATRIX -> darkColorScheme(

            background = Color(0xFF020402),
            surface = Color(0xFF0B120B),

            primary = Color(0xFF3CFF88),
            secondary = Color(0xFF1FAF5C),
            tertiary = Color(0xFF8DFFC0),

            onPrimary = Color.Black,

            onBackground = Color(0xFFE4FFE9),
            onSurface = Color(0xFFE4FFE9)
        )
    }
}