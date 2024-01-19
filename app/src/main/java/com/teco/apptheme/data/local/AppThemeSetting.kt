package com.teco.apptheme.data.local

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class AppThemeSetting(
    val themeType: ThemeType = ThemeType.Device,
    val isDynamicThemeEnables: Boolean = false
)

object AppThemeSettingSerializer : Serializer<AppThemeSetting> {
    override val defaultValue: AppThemeSetting = AppThemeSetting()

    override suspend fun readFrom(input: InputStream): AppThemeSetting {
        return try {
            Json.decodeFromString(
                AppThemeSetting.serializer(), input.readBytes().decodeToString()
            )
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppThemeSetting, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(AppThemeSetting.serializer(), t).encodeToByteArray()
            )
        }
    }

}

val Context.appThemeSettingDataStore by dataStore("AppThemeSetting.json", AppThemeSettingSerializer)