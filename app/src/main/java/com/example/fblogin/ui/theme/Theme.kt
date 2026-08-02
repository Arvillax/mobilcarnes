package com.example.fblogin.ui.theme

// COMENTADO: FbloginTheme no se usa — MainActivity usa NavGraph directo sin wrapper de tema
/*
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// esquema de colores oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Crimson,
    secondary = Coral,
    tertiary = Rosa,
    background = VinoDark,
    surface = Vino,
    onPrimary = White,
    onSecondary = White,
    onTertiary = VinoDark,
    onBackground = White,
    onSurface = White,
    surfaceVariant = Carmesi,
    onSurfaceVariant = RosaSoft,
    outline = Coral
)

// esquema de colores claro
private val LightColorScheme = lightColorScheme(
    primary = Carmesi,
    secondary = Crimson,
    tertiary = Coral,
    background = White,
    surface = GrayLight,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = GrayDark,
    onSurface = GrayDark,
    surfaceVariant = RosaSoft,
    onSurfaceVariant = GrayDark,
    outline = GrayMedium
)

@Composable
fun FbloginTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
*/
