package code.viewer.ui.editor

import androidx.compose.runtime.mutableStateListOf
import code.viewer.platform.File
import code.viewer.util.SingleSelection

// Editors → Editor
class Editors {

    private val selection = SingleSelection()

    var editors = mutableStateListOf<Editor>()
        private set

    val active: Editor? get() = selection.selected as Editor?

    // ファイルを選択した時
    fun open(file: File) {
        val editor = Editor(file)
        editor.selection = selection
        editor.close = {
            close(editor)
        }

        // 配列に選択したファイルオブジェクトを追加、エディターTabの描画に反映される
        editors.add(editor)

        // 開いたファイルがアクティブと見なされる
        editor.activate()
    }

    // ファイルを閉じた時
    private fun close(editor: Editor) {
        val index = editors.indexOf(editor)
        editors.remove(editor)
        if (editor.isActive) {
            // coerceAtMost: 配列からオブジェクトを除去した後、lastIndexの値以上の返り値が返却されないようにする
            selection.selected = editors.getOrNull(index.coerceAtMost(editors.lastIndex))
        }
    }
}