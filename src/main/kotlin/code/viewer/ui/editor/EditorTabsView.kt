package code.viewer.ui.editor

import androidx.compose.animation.animate
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import code.viewer.ui.AppTheme

// 選択したファイルの数だけTabを描画する
@Composable
fun EditorTabsView(model: Editors) = ScrollableRow {
    for (editor in model.editors) {
        EditorTabView(editor)
    }
}

// 1Tabの描画内容
@Composable
fun EditorTabView(model: Editor) = Surface(
    // animate:色の変更を管理する為の関数？
    color = animate(
        // Modifier.clickableの処理と連動して、アクティブなタブの色を変化させる
        if (model.isActive) {
            AppTheme.colors.backgroundDark
        } else {
            Color.Transparent
        }
    )
) {
    Row(
        Modifier.clickable {
            model.activate()
        }.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // タブに表示されるファイル名
        Text(
            model.fileName,
            color = AmbientContentColor.current,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        val close = model.close

        if (close != null) {
            // 閉じる×ボタンのアイコン
            Icon(
                Icons.Default.Close, tint = AmbientContentColor.current, modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
                    .clickable {
                        close()
                    })
        } else {
            Box(modifier = Modifier.size(24.dp, 24.dp).padding(4.dp))
        }
    }
}