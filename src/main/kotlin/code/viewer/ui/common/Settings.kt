package code.viewer.ui.common

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class Settings {
    var fontSize by mutableStateOf(13.sp)
    val maxLineSymbols = 120

}