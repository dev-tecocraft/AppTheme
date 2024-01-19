package com.teco.apptheme.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.teco.apptheme.data.local.AppThemeSetting
import com.teco.apptheme.data.local.ThemeType

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AppTheme(
    appThemeSetting: AppThemeSetting,
    content: @Composable () -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val context = LocalContext.current
    var isSystemUIForegroundColorDark = false
    val colorScheme =
        if (appThemeSetting.isDynamicThemeEnables && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when (appThemeSetting.themeType) {
                ThemeType.Device ->
                    if (darkTheme) {
                        isSystemUIForegroundColorDark = false
                        dynamicDarkColorScheme(context)
                    } else {
                        isSystemUIForegroundColorDark = true
                        dynamicLightColorScheme(context)
                    }

                ThemeType.Light -> {
                    isSystemUIForegroundColorDark = true
                    dynamicLightColorScheme(context)
                }

                ThemeType.Dark -> {
                    isSystemUIForegroundColorDark = false
                    dynamicDarkColorScheme(context)
                }
            }
        } else {
            when (appThemeSetting.themeType) {
                ThemeType.Device -> if (darkTheme) {
                    isSystemUIForegroundColorDark = false
                    DarkColorScheme
                } else {
                    isSystemUIForegroundColorDark = true
                    LightColorScheme
                }

                ThemeType.Light -> {
                    isSystemUIForegroundColorDark = true
                    LightColorScheme
                }

                ThemeType.Dark -> {
                    isSystemUIForegroundColorDark = false
                    DarkColorScheme
                }
            }
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val appSystemUiControllerBackground = Color(0x26252525).toArgb()
            window.navigationBarColor = appSystemUiControllerBackground
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                isSystemUIForegroundColorDark
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                isSystemUIForegroundColorDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}