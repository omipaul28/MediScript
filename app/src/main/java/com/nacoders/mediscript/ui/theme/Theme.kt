package com.nacoders.mediscript.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(

    primary = MedicalBlue,
    onPrimary = BackgroundWhite,

    primaryContainer = MedicalBlueLight,
    onPrimaryContainer = BackgroundWhite,

    secondary = MedicalBlueDark,
    background = BackgroundWhite,
    surface = SurfaceWhite,

    onBackground = TextPrimary,
    onSurface = TextPrimary,

    error = ErrorRed
)

@Composable
fun SmartPrescriptionTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}