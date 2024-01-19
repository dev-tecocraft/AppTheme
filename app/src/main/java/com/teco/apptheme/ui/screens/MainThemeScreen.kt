@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.teco.apptheme.ui.screens

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.teco.apptheme.R
import com.teco.apptheme.data.local.AppThemeSetting
import com.teco.apptheme.data.local.ThemeType
import com.teco.apptheme.data.local.appThemeSettingDataStore
import com.teco.apptheme.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun MainThemeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val appThemeSetting by context.appThemeSettingDataStore.data.collectAsState(initial = AppThemeSetting())
    val coroutineScope = rememberCoroutineScope()
    var isShowThemeChooserDialog by remember {
        mutableStateOf(
            false
        )
    }
    var currentSelectedAppTheme by remember {
        mutableStateOf(appThemeSetting.themeType)
    }
    if (isShowThemeChooserDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            onDismissRequest = {
                isShowThemeChooserDialog = false
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.choose_app_theme),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Divider(
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    ThemeType.entries.forEach { themeType ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = themeType == currentSelectedAppTheme,
                                onClick = {
                                    currentSelectedAppTheme = themeType
                                },
                                enabled = true,
                            )
                            Text(text = themeType.name, fontSize = 20.sp)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = {
                            currentSelectedAppTheme = appThemeSetting.themeType
                            isShowThemeChooserDialog = false
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(onClick = {
                            coroutineScope.launch {
                                updateAppThemeSetting(
                                    context,
                                    appThemeSetting.copy(
                                        themeType = currentSelectedAppTheme
                                    )
                                )
                            }
                            isShowThemeChooserDialog = false
                        }) {
                            Text(text = stringResource(R.string.change_theme))
                        }
                    }
                }
            }
        }
    }
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    isShowThemeChooserDialog = true
                },
            ) {
                Text(
                    text = stringResource(R.string.current_theme, appThemeSetting.themeType),
                    fontSize = 18.sp
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.enable_dynamic_theme),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Switch(
                        checked = appThemeSetting.isDynamicThemeEnables,
                        onCheckedChange = {
                            coroutineScope.launch {
                                updateAppThemeSetting(
                                    context,
                                    appThemeSetting.copy(
                                        isDynamicThemeEnables = it
                                    )
                                )
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Divider(
                color = MaterialTheme.colorScheme.onSurface
            )
            DefaultComponentViews()
        }
    }
}

@Composable
fun DefaultComponentViews() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ButtonComponents()
        }
        item {
            SwitchComponents()
        }
        item {
            ChipsComponents()
        }
        item {
            SliderComponents()
        }
        item {
            FloatingButtonComponents()
        }
        item {
            LoadersComponents()
        }
    }
}

@Composable
fun LoadersComponents() {
    OutlinedCard(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.loader_progress), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            FlowColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LinearProgressIndicator(
                    progress = 0.5F,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                CircularProgressIndicator(
                    progress = 0.5F,
                    modifier = Modifier.width(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                CircularProgressIndicator(
                    modifier = Modifier.width(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.size(1.dp))
            }
        }
    }
}

@Composable
fun FloatingButtonComponents() {
    Card(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.floating_buttons), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(
                    onClick = {  },
                ) {
                    Icon(Icons.Filled.Add, "")
                }
                SmallFloatingActionButton(
                    onClick = {  },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Filled.Add, "")
                }
                ExtendedFloatingActionButton(
                    onClick = {  },
                    icon = { Icon(Icons.Filled.Edit, "") },
                    text = { Text(text = stringResource(R.string.extended_fab)) },
                )
                LargeFloatingActionButton(
                    onClick = {  },
                    shape = CircleShape,
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    }
}

@Composable
fun ChipsComponents() {
    var selected by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.chips), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(stringResource(R.string.assist_chip)) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
                FilterChip(
                    onClick = { selected = !selected },
                    label = {
                        Text(stringResource(R.string.filter_chip))
                    },
                    selected = selected,
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
                SuggestionChip(
                    onClick = {  },
                    label = { Text(stringResource(R.string.suggestion_chip)) }
                )

                InputChip(
                    onClick = {
                              enabled = !enabled
                    },
                    label = { Text(stringResource(R.string.input_chip)) },
                    selected = enabled,
                    avatar = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "",
                            Modifier.size(InputChipDefaults.AvatarSize)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            Modifier.size(InputChipDefaults.AvatarSize)
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun SliderComponents() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var rangeSliderPosition by remember { mutableStateOf(0f..100f) }
    OutlinedCard(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.sliders), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it }
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 3,
                    valueRange = 0f..50f
                )
                RangeSlider(
                    value = rangeSliderPosition,
                    steps = 5,
                    onValueChange = { range -> rangeSliderPosition = range },
                    valueRange = 0f..100f,
                    onValueChangeFinished = {
                    },
                )
            }
        }
    }
}

@Composable
fun SwitchComponents() {
    OutlinedCard(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.switches), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Switch(
                    checked = false,
                    onCheckedChange = {
                    }
                )
                Switch(
                    checked = true,
                    onCheckedChange = {
                    }
                )
                Switch(
                    checked = false,
                    onCheckedChange = {},
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                )
                Switch(
                    checked = true,
                    onCheckedChange = {},
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                )
                Switch(
                    checked = false,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
                Switch(
                    checked = true,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.onSecondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }
        }
    }
}

@Composable
fun ButtonComponents() {
    Card(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.buttons), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { }) {
                    Text(text = stringResource(R.string.button))
                }
                FilledTonalButton(onClick = { }) {
                    Text(text = stringResource(R.string.filled_tonal_button))
                }
                OutlinedButton(onClick = { }) {
                    Text(text = stringResource(R.string.outline_button))
                }
                ElevatedButton(onClick = { }) {
                    Text(text = stringResource(R.string.elevated_button))
                }
                TextButton(onClick = { }) {
                    Text(text = stringResource(R.string.text_button))
                }
            }
        }
    }

}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    AppTheme(appThemeSetting = AppThemeSetting()) {
        MainThemeScreen()
    }
}

suspend fun updateAppThemeSetting(context: Context, updatedAppThemeSetting: AppThemeSetting) {
    context.appThemeSettingDataStore.updateData {
        it.copy(
            themeType = updatedAppThemeSetting.themeType,
            isDynamicThemeEnables = updatedAppThemeSetting.isDynamicThemeEnables,
        )
    }
}