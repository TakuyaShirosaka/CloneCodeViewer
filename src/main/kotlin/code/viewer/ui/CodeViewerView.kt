package code.viewer.ui

import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import code.viewer.ui.editor.EditorEmptyView
import code.viewer.ui.editor.EditorTabsView
import code.viewer.ui.editor.EditorView
import code.viewer.ui.filetree.FileTreeView
import code.viewer.ui.filetree.FileTreeViewTabView
import code.viewer.ui.statusbar.StatusBar
import code.viewer.util.SplitterState
import code.viewer.util.VerticalSplittable

@Composable
fun CodeViewerView(model: CodeViewer) {

    val panelState = remember { PanelState() }
    val animatedSize = if (panelState.splitter.isResizing) {
        if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize
    } else {
        val animate = animate(
            if (panelState.isExpanded) panelState.expandedSize else panelState.collapsedSize,
            SpringSpec(stiffness = StiffnessLow)
        )
        animate
    }

    VerticalSplittable(
        Modifier.fillMaxSize(),
        panelState.splitter,
        onResize = {
            panelState.expandedSize =
                (panelState.expandedSize + it).coerceAtLeast(panelState.expandedSizeMin)
        }
    ) {
        ResizablePanel(Modifier.width(animatedSize).fillMaxHeight(), panelState) {
            Column {
                FileTreeViewTabView()
                FileTreeView(model.fileTree)
            }
        }

        Box {
            if (model.editors.active != null) {
                Column(Modifier.fillMaxSize()) {
                    EditorTabsView(model.editors)
                    Box(Modifier.weight(1f)) {
                        EditorView(model.editors.active!!, model.settings)
                    }
                    StatusBar(model.settings)
                }
            } else {
                EditorEmptyView()
            }
        }
    }
}

private class PanelState {
    val collapsedSize = 24.dp
    var expandedSize by mutableStateOf(300.dp)
    val expandedSizeMin = 90.dp
    var isExpanded by mutableStateOf(true)
    val splitter = SplitterState()
}

@Composable
private fun ResizablePanel(
    modifier: Modifier,
    state: PanelState,
    content: @Composable () -> Unit,
) {
    val alpha = animate(if (state.isExpanded) 1f else 0f, SpringSpec(stiffness = StiffnessLow))

    Box(modifier) {
        Box(Modifier.fillMaxSize().graphicsLayer(alpha = alpha)) {
            content()
        }

        Icon(
            if (state.isExpanded) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
            tint = AmbientContentColor.current,
            modifier = Modifier.padding(top = 4.dp)
                .width(24.dp)
                .clickable {
                    state.isExpanded = !state.isExpanded
                }
                .padding(4.dp)
                .align(Alignment.TopEnd)
        )
    }
}