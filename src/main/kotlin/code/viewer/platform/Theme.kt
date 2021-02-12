package code.viewer.platform

import androidx.compose.desktop.DesktopTheme
import androidx.compose.runtime.Composable

@Composable
fun PlatformTheme(content: @Composable () -> Unit) = DesktopTheme(content = content)