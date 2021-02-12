package code.viewer.platform

import androidx.compose.desktop.AppWindowAmbient
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerMoveFilter
import java.awt.Cursor
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.composed

// マウスアクションの追加
fun Modifier.pointerMoveFilter(
    onEnter: () -> Boolean,
    onExit: () -> Boolean,
    onMove: (Offset) -> Boolean
): Modifier = this.pointerMoveFilter(onEnter = onEnter, onExit = onExit, onMove = onMove)


// リサイズに関するアクションを拡張関数を使用してModifierに付与
fun Modifier.cursorForHorizontalResize(): Modifier = composed {
    var isHover by remember { mutableStateOf(false) }
    if (isHover) {
        // 東方向サイズ変更のカーソルタイプです。
        AppWindowAmbient.current!!.window.cursor = Cursor(Cursor.E_RESIZE_CURSOR)
    } else {
        // デフォルトで使えるカーソル
        AppWindowAmbient.current!!.window.cursor = Cursor.getDefaultCursor()

    }
    pointerMoveFilter(
        onEnter = { isHover = true; true },
        onExit = { isHover = false; true }
    )
}