package code.viewer.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.material.Surface
import androidx.compose.ui.selection.DisableSelection
import code.viewer.platform.HomeFolder
import code.viewer.platform.PlatformTheme
import code.viewer.ui.common.Settings
import code.viewer.ui.editor.Editors
import code.viewer.ui.filetree.FileTree

// UIコンポーネントはfun形式で返却値はUnit
// @Composableのアノテーションが必ず必要
@Composable
fun MainView() {

    // remember:状態管理を行うための関数、
    // 内部の値は初回の表示時に保存され再描画の際も返される
    val codeViewer = remember {
        val editors = Editors()

        CodeViewer(
            editors = editors,
            fileTree = FileTree(HomeFolder, editors),
            settings = Settings()
        )
    }

    // テキスト選択無効
    DisableSelection {
        MaterialTheme(
            colors = AppTheme.colors.material
        ) {
            PlatformTheme {
                /**
                 * Surface:マテリアルデザインの中心的なコンポーネント
                 * content: @Composable()の内容をマテリアルデザインのルールにのっとって描画する役割を持つ
                 */
                Surface {
                    CodeViewerView(codeViewer)
                }
            }
        }
    }
}