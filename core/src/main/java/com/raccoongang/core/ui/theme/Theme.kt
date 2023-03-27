package com.raccoongang.core.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = AppColors(
    material = darkColors(
        primary = Color(0xFFDF7A5E),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        secondaryVariant = Color(0xFF373E4F),
        background = Color(0xFF3C405B),
        surface = Color(0xFF3C405B),
        error = Color(0xFFFF3D71),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.Black
    ),
    textPrimary = Color.White,
    textPrimaryVariant = Color(0xFF79889F),
    textSecondary = Color(0xFFB3B3B3),
    textDark = Color(0xFF19212F),
    textAccent = Color(0xCCDF7A5E),

    textFieldBackground = Color(0xFF585D81),
    textFieldBackgroundVariant = Color(0xFF273346),
    textFieldBorder = Color(0xFF97A5BB),
    textFieldText = Color.White,
    textFieldHint = Color(0xFF79889F),

    buttonBackground = Color(0xFFDF7A5E),
    buttonText = Color.White,

    cardViewBackground = Color(0xFF3C405B),
    cardViewBorder = Color(0xFF3C405B),

    certificateForeground = Color(0xD92EB865),
    bottomSheetToggle = Color(0xFF4E5A70),

    warning = Color(0xFFFFC248),
    info = Color(0xFF0095FF)
)

private val LightColorPalette = AppColors(
    material = lightColors(
        primary = Color(0xFF307A59),
        primaryVariant = Color(0x9ADEFAFF),
        secondary = Color(0xFF94D3DD),
        secondaryVariant = Color(0xFF94D3DD),
        background = Color.White,
        surface = Color(0xFFF7F7F8),
        error = Color(0xFFFF3D71),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        onError = Color.White
    ),
    textPrimary = Color(0xFF212121),
    textPrimaryVariant = Color(0xFF3D4964),
    textSecondary = Color(0xFFB3B3B3),
    textDark = Color(0xFF19212F),
    textAccent = Color(0xCC307A59),

    textFieldBackground = Color(0xFFF7F7F8),
    textFieldBackgroundVariant = Color.White,
    textFieldBorder = Color(0xFF97A5BB),
    textFieldText = Color(0xFF3D4964),
    textFieldHint = Color(0xFF97A5BB),

    buttonBackground = Color(0xFF307A59),
    buttonText = Color.White,

    cardViewBackground = Color.White,
    cardViewBorder = Color(0xFFCCD4E0),

    certificateForeground = Color(0xD94BD191),
    bottomSheetToggle = Color(0xFF4E5A70),

    warning = Color(0xFFFFC94D),
    info = Color(0xFF42AAFF)
)

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = if (colors.isLight) LightColorPalette else DarkColorPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewEdxTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors.material,
        //typography = LocalTypography.current.material,
        shapes = LocalShapes.current.material,
    ) {
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null,
            content = content
        )
    }
}
