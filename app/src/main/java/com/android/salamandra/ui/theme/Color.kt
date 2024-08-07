package com.android.salamandra.ui.theme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFFFF6000)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFE165)
val md_theme_light_onPrimaryContainer = Color(0xFF221B00)
val md_theme_light_secondary = Color(0xFF7E5700)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDEAC)
val md_theme_light_onSecondaryContainer = Color(0xFF281900)
val md_theme_light_tertiary = Color(0xFFA63C00)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFDBCE)
val md_theme_light_onTertiaryContainer = Color(0xFF370E00)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF1D1B16)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF1D1B16)
val md_theme_light_surfaceVariant = Color(0xFFEAE2CF)
val md_theme_light_onSurfaceVariant = Color(0xFF4B4739)
val md_theme_light_outline = Color(0xFF7C7767)
val md_theme_light_inverseOnSurface = Color(0xFFF6F0E7)
val md_theme_light_inverseSurface = Color(0xFF32302A)
val md_theme_light_inversePrimary = Color(0xFFE7C400)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF6F5D00)
val md_theme_light_outlineVariant = Color(0xFFCDC6B4)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFFFF6000)
val md_theme_dark_onPrimary = Color(0xFF3A3000)
val md_theme_dark_primaryContainer = Color(0xFF544600)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFE165)
val md_theme_dark_secondary = Color(0xFFFFBA38)
val md_theme_dark_onSecondary = Color(0xFF432C00)
val md_theme_dark_secondaryContainer = Color(0xFF604100)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFDEAC)
val md_theme_dark_tertiary = Color(0xFFFFB598)
val md_theme_dark_onTertiary = Color(0xFF591C00)
val md_theme_dark_tertiaryContainer = Color(0xFF7E2C00)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFDBCE)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1D1B16)
val md_theme_dark_onBackground = Color(0xFFE7E2D9)
val md_theme_dark_surface = Color(0xFF1D1B16)
val md_theme_dark_onSurface = Color(0xFFE7E2D9)
val md_theme_dark_surfaceVariant = Color(0xFF4B4739)
val md_theme_dark_onSurfaceVariant = Color(0xFFCDC6B4)
val md_theme_dark_outline = Color(0xFF969080)
val md_theme_dark_inverseOnSurface = Color(0xFF1D1B16)
val md_theme_dark_inverseSurface = Color(0xFFE7E2D9)
val md_theme_dark_inversePrimary = Color(0xFF6F5D00)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFE7C400)
val md_theme_dark_outlineVariant = Color(0xFF4B4739)
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Color(0xFFFFD900)
val salamandraColor = Color(0xFFFF6000)
val light_CustomColor1 = Color(0xFFA63C00)
val light_onCustomColor1 = Color(0xFFFFFFFF)
val light_CustomColor1Container = Color(0xFFFFDBCE)
val light_onCustomColor1Container = Color(0xFF370E00)
val dark_CustomColor1 = Color(0xFFFFB598)
val dark_onCustomColor1 = Color(0xFF591C00)
val dark_CustomColor1Container = Color(0xFF7E2C00)
val dark_onCustomColor1Container = Color(0xFFFFDBCE)


//Color Palette
val primary = Color(0xffff6000)
val primaryVariant = Color(0xffff7827)
val onPrimary = Color(0xFFFFFFFF)

val secondary = Color(0xff181818)
val onSecondary = Color(0xff7b7b7b)
val secondaryVariant = Color(0xff2f2f2f)
val onSecondaryVariant = Color(0xffbbbbbb)


val tertiary = Color(0xff101010)
val onTertiary = Color(0xff6b6b6b)

val bottomBar = Color(0xff0d0d0d)
val title = Color(0xFFFFFFFF)
val subtitle = Color(0xFFe6e6e6)

val colorError = Color(0xffd84848)

val colorMessage = Color(0xfffdf56a).copy(alpha = 0.7f)
val selectedScreenBottomBar =Color(0xffd3d3d3).copy(alpha = 0.3f)

@Composable
fun textFieldColors (
    visibleCursor: Boolean = true
): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedTextColor = title,
        focusedContainerColor = onTertiary.copy(0.2f),
        unfocusedTextColor = subtitle,
        unfocusedContainerColor = secondary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledTextColor = secondary.copy(0.5f),
        cursorColor = if (!visibleCursor) Color.Transparent else primaryVariant
    )
}
