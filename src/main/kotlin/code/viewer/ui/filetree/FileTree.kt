package code.viewer.ui.filetree

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import code.viewer.platform.File
import code.viewer.ui.editor.Editors

class ExpandableFile(
    val file: File,
    val level: Int
) {
    var children: List<ExpandableFile> by mutableStateOf(emptyList())
    val canExpand: Boolean get() = file.hasChildren

    // フォルダの中身を展開
    fun toggleExpanded() {
        children = if (children.isEmpty()) {
            file.children
                .map { ExpandableFile(it, level + 1) }
                .sortedWith(compareBy({ it.file.isDirectory }, { it.file.name }))
                .sortedBy { !it.file.isDirectory }
        } else {
            emptyList()
        }
    }
}

// root:ホームディレクトリの情報が入っている
class FileTree(root: File, private val editors: Editors) {

    private val expandedRoot = ExpandableFile(root, 0).apply {
        // root直下の展開
        toggleExpanded()
    }

    val items: List<Item> get() = expandedRoot.toItems()

    // 各ファイルの属するクラス
    inner class Item constructor(
        private val file: ExpandableFile
    ) {
        val name: String get() = file.file.name
        val level: Int get() = file.level
        val type: ItemType
            get() = if (file.file.isDirectory) {
                ItemType.Folder(isExpanded = file.children.isNotEmpty(), canExpand = file.canExpand)
            } else {
                ItemType.File(ext = file.file.name.substringAfterLast(".").toLowerCase())
            }

        //ファイルを選択した時、
        //ディレクトリは中身を展開、ファイルはファイルを開くときのイベントを発火(Editors.openにつながる)
        fun open() = when (type) {
            is ItemType.Folder -> file.toggleExpanded()
            is ItemType.File -> editors.open(file.file)
        }
    }

    /**
     * sealed
     * クラスの継承を制限するための修飾子です。
     * FileTreeViewでのアイコンの出し分けに使用
     * */
    sealed class ItemType {
        class Folder(val isExpanded: Boolean, val canExpand: Boolean) : ItemType()
        class File(val ext: String) : ItemType()
    }

    /** 拡張関数 */
    // rootより下のディレクトリたちをExpandableFileインスタンス化してListにする
    private fun ExpandableFile.toItems(): List<Item> {
        fun ExpandableFile.addTo(list: MutableList<Item>) {
            list.add(Item(this))
            for (child in children) {
                child.addTo(list)
            }
        }

        val list = mutableListOf<Item>()
        addTo(list)
        return list
    }

}