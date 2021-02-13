package code.viewer.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.DesktopSelectionContainer
import androidx.compose.ui.selection.Selection
import androidx.compose.ui.text.InternalTextApi

@OptIn(InternalTextApi::class)
@Composable
fun SelectionContainer(children: @Composable () -> Unit) {
    val selection = remember { mutableStateOf<Selection?>(null) }
    DesktopSelectionContainer(
        selection = selection.value,
        onSelectionChange = { selection.value = it },
        content = children
    )
}