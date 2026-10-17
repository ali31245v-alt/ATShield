package com.at.shield
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
@Composable fun ShieldTheme(content: @Composable () -> Unit) { CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { MaterialTheme { Surface(Modifier.fillMaxSize(), content=content) } } }
