package code.viewer.ui.editor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import code.viewer.platform.File
import code.viewer.util.EmptyTextLines
import code.viewer.util.SingleSelection

class Editor(
    val fileName: String,
    val lines: (backgroundScope: CoroutineScope) -> Lines, // ← 最後のパラメータがLambda関数
) {

    // Unit型の関数を受け入れる変数
    var close: (() -> Unit)? = null

    lateinit var selection: SingleSelection

    // 今このファイルオブジェクトが選択されているかどうかの判定
    val isActive: Boolean
        get() = selection.selected === this

    fun activate() {
        selection.selected = this
    }

    class Line(val number: Int, val content: Content)

    interface Lines {
        val lineNumberDigitCount: Int get() = size.toString().length
        val size: Int
        operator fun get(index: Int): Line
    }

    class Content(val value: State<String>, val isCode: Boolean)
}

/**
 * <trailing lambda 記法>
 *     backgroundScope -> ~の部分
 *     Kotlinには、規則があります。関数の最後のパラメーターが関数の場合、
 *     対応する引数として渡されるラムダ式を括弧の外に配置できます。
 */
fun Editor(file: File) = Editor(
    fileName = file.name
) { backgroundScope ->
    val textLines = try {
        file.readLines(backgroundScope)
    } catch (e: Throwable) {
        println(e.message)
        EmptyTextLines
    }
    val isCode = file.name.endsWith(".kt", ignoreCase = true)

    fun content(index: Int): Editor.Content {
        val text = textLines.get(index)
        val state = mutableStateOf(text)
        return Editor.Content(state, isCode)
    }

    object : Editor.Lines {
        override val size get() = textLines.size

        override fun get(index: Int) = Editor.Line(
            number = index + 1,
            content = content(index)
        )
    }
}
