package de.geier.citymanager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

fun resolveTypography(
    base: Typography,
    preset: FontPreset,
    stylePreset: CityStylePreset,
    fontScale: Float
): Typography {

    val headlineFont = when (preset) {
        FontPreset.DEFAULT -> FontFamily.Default
        FontPreset.MEDIEVAL -> MedievalFont
        FontPreset.SCIFI -> ScifiFont
        FontPreset.WESTERN -> WesternFont
        FontPreset.ASIA -> AsiaFont
    }

    val bodyFont = FontFamily.Default

    val headlineScale = when (preset) {
        FontPreset.SCIFI -> 1.08f
        FontPreset.MEDIEVAL -> 1.22f
        FontPreset.WESTERN -> 1.10f
        FontPreset.ASIA -> 1.18f
        FontPreset.DEFAULT -> 1.0f
    }

    val titleScale = when (preset) {
        FontPreset.SCIFI -> 1.06f
        FontPreset.MEDIEVAL -> 1.18f
        FontPreset.WESTERN -> 1.08f
        FontPreset.ASIA -> 1.14f
        FontPreset.DEFAULT -> 1.0f
    }

    val headlineSpacing = when (stylePreset) {

        CityStylePreset.URBAN_GREY -> 1.5.sp

        CityStylePreset.PARCHEMENT -> 0.3.sp

        CityStylePreset.BLOSSOM -> 0.sp

        CityStylePreset.DUST -> 0.8.sp

        CityStylePreset.FILM_NOIR -> 0.2.sp

        CityStylePreset.NEON_MATRIX -> 2.sp
    }

    return Typography(

        /* ---------------- Display ---------------- */

        displayLarge = base.displayLarge.copy(
            fontFamily = headlineFont,
            fontSize = base.displayLarge.fontSize * fontScale
        ),

        displayMedium = base.displayMedium.copy(
            fontFamily = headlineFont,
            fontSize = base.displayMedium.fontSize * fontScale
        ),

        displaySmall = base.displaySmall.copy(
            fontFamily = headlineFont,
            fontSize = base.displaySmall.fontSize * fontScale
        ),

        /* ---------------- Headlines ---------------- */

        headlineLarge = base.headlineLarge.copy(
            fontFamily = headlineFont,
            letterSpacing = headlineSpacing,
            fontSize =
                base.headlineLarge.fontSize *
                        headlineScale *
                        fontScale
        ),

        headlineMedium = base.headlineMedium.copy(
            fontFamily = headlineFont,
            letterSpacing = headlineSpacing,
            fontSize =
                base.headlineMedium.fontSize *
                        headlineScale *
                        fontScale
        ),

        headlineSmall = base.headlineSmall.copy(
            fontFamily = headlineFont,
            letterSpacing = headlineSpacing,
            fontSize =
                base.headlineSmall.fontSize *
                        headlineScale *
                        fontScale
        ),

        /* ---------------- Titles ---------------- */

        titleLarge = base.titleLarge.copy(
            fontFamily = headlineFont,
            fontSize =
                base.titleLarge.fontSize *
                        titleScale *
                        fontScale
        ),

        titleMedium = base.titleMedium.copy(
            fontFamily = headlineFont,
            fontSize =
                base.titleMedium.fontSize *
                        titleScale *
                        fontScale
        ),

        titleSmall = base.titleSmall.copy(
            fontFamily = headlineFont,
            fontSize =
                base.titleSmall.fontSize *
                        titleScale *
                        fontScale
        ),

        /* ---------------- Body ---------------- */

        bodyLarge = base.bodyLarge.copy(
            fontFamily = bodyFont,
            fontSize = base.bodyLarge.fontSize * fontScale
        ),

        bodyMedium = base.bodyMedium.copy(
            fontFamily = bodyFont,
            fontSize = base.bodyMedium.fontSize * fontScale
        ),

        bodySmall = base.bodySmall.copy(
            fontFamily = bodyFont,
            fontSize = base.bodySmall.fontSize * fontScale
        ),

        /* ---------------- Labels ---------------- */

        labelLarge = base.labelLarge.copy(
            fontFamily = headlineFont,
            fontSize =
                base.labelLarge.fontSize *
                        titleScale *
                        fontScale
        ),

        labelMedium = base.labelMedium.copy(
            fontFamily = headlineFont,
            fontSize =
                base.labelMedium.fontSize *
                        titleScale *
                        fontScale
        ),

        labelSmall = base.labelSmall.copy(
            fontFamily = headlineFont,
            fontSize =
                base.labelSmall.fontSize *
                        titleScale *
                        fontScale
        ),
    )
}