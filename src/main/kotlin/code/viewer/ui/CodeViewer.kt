package code.viewer.ui

import code.viewer.ui.common.Settings
import code.viewer.ui.editor.Editors
import code.viewer.ui.filetree.FileTree

class CodeViewer(
    val editors: Editors,
    val fileTree: FileTree,
    val settings: Settings
)