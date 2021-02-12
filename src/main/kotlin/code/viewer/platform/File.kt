package code.viewer.platform

import kotlinx.coroutines.CoroutineScope
import code.viewer.util.TextLines

// ユーザーのホーム・ディレクトリ
val HomeFolder: File = java.io.File(System.getProperty("user.home")).toProjectFile()

interface File {
    val name: String
    val isDirectory: Boolean
    val children: List<File>
    val hasChildren: Boolean

    fun readLines(scope: CoroutineScope): TextLines
}