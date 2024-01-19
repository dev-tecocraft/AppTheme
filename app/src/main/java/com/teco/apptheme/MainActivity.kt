package com.teco.apptheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.teco.apptheme.data.local.AppThemeSetting
import com.teco.apptheme.data.local.appThemeSettingDataStore
import com.teco.apptheme.ui.navigation.AppNavigation
import com.teco.apptheme.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appThemeSetting by appThemeSettingDataStore.data.collectAsState(initial = AppThemeSetting())

            AppTheme(
                appThemeSetting
            ) {
                AppNavigation()
            }
            /*AppTheme {
                val showCompleted by dataStore.data.collectAsState(initial = UserPreferences())
                val scope = rememberCoroutineScope()
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        scope.launch {
                            updateData()
                        }
                    }) {
                        Text(text = "ShowCompleted :: ${showCompleted.showCompleted}")
                    }
                }
            }*/
        }
    }
}

