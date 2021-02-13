package code.viewer.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.selection.DisableSelection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import code.viewer.platform.SelectionContainer
import code.viewer.platform.VerticalScrollbar
import code.viewer.ui.AppTheme
import code.viewer.ui.common.Fonts
import code.viewer.ui.common.Settings
import code.viewer.util.LazyColumnFor
import code.viewer.util.loadable
import code.viewer.util.loadableScoped
import code.viewer.util.withoutWidthConstraints
import kotlin.text.Regex.Companion.fromLiteral

@Composable
fun EditorView(model: Editor, settings: Settings) = key(model) {
    with(AmbientDensity.current) {
        SelectionContainer {
            Surface(
                Modifier.fillMaxSize(),
                color = AppTheme.colors.backgroundDark,
            ) {
                val lines by loadableScoped(model.lines)

                if (lines != null) {
                    Box {
                        Lines(lines!!, settings)
                        // エディター右側部分
                        Box(
                            Modifier
                                .offset(
                                    x = settings.fontSize.toDp() * 0.5f * settings.maxLineSymbols
                                )
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(AppTheme.colors.backgroundLight)
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Lines(lines: Editor.Lines, settings: Settings) = with(AmbientDensity.current) {

    // ファイルの行数からそのファイルが最大何桁表示するのかを計算
    val maxNumber = remember(lines.lineNumberDigitCount) {
        (1..lines.lineNumberDigitCount).joinToString(separator = "") { "9" }
    }

    Box(Modifier.fillMaxSize()) {
        val scrollState = rememberLazyListState()
        val lineHeight = settings.fontSize.toDp() * 1.6f

        LazyColumnFor(
            lines.size,
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            itemContent = { index ->
                // 行ごとの描画内容
                val line: Editor.Line? by loadable { lines[index] }
                Box(Modifier.height(lineHeight)) {
                    if (line != null) {
                        Line(Modifier.align(Alignment.CenterStart), maxNumber, line!!, settings)
                    }
                }
            }
        )

        VerticalScrollbar(
            Modifier.align(Alignment.CenterEnd),
            scrollState,
            lines.size,
            lineHeight
        )
    }
}


@Composable
private fun Line(modifier: Modifier, maxNum: String, line: Editor.Line, settings: Settings) {
    Row(modifier = modifier) {
        DisableSelection {
            Box {
                // 1個目 spaceのパディング的な、透明な文字を左詰に入れている(999とか)
                LineNumber(maxNum, Modifier.alpha(0f), settings)
                // 2個目 行番号を右端詰で表示する
                LineNumber(line.number.toString(), Modifier.align(Alignment.CenterEnd), settings)
            }
        }
        LineContent(
            line.content,
            modifier = Modifier.weight(1f).withoutWidthConstraints().padding(start = 28.dp, end = 12.dp), settings
        )
    }
}

// エディター左端の行番号の部分
@Composable
private fun LineNumber(number: String, modifier: Modifier, settings: Settings) = Text(
    text = number,
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    color = AmbientContentColor.current.copy(alpha = 0.30f),
    modifier = modifier.padding(start = 12.dp)
)

// 行の本文
@Composable
private fun LineContent(content: Editor.Content, modifier: Modifier, settings: Settings) = Text(
    text = if (content.isCode) {
        codeString(content.value.value)
    } else {
        buildAnnotatedString {
            withStyle(AppTheme.code.simple) {
                append(content.value.value)
            }
        }
    },
    fontSize = settings.fontSize,
    fontFamily = Fonts.jetbrainsMono(),
    modifier = modifier,
    softWrap = false
)

// 特定文字のハイライト的な
private fun codeString(str: String) = buildAnnotatedString {
    withStyle(AppTheme.code.simple) {
        append(str.replace("\t", "    "))
        addStyle(AppTheme.code.punctuation, ":")
        addStyle(AppTheme.code.punctuation, "=")
        addStyle(AppTheme.code.punctuation, "\"")
        addStyle(AppTheme.code.punctuation, "[")
        addStyle(AppTheme.code.punctuation, "]")
        addStyle(AppTheme.code.punctuation, "{")
        addStyle(AppTheme.code.punctuation, "}")
        addStyle(AppTheme.code.punctuation, "(")
        addStyle(AppTheme.code.punctuation, ")")
        addStyle(AppTheme.code.punctuation, ",")
        addStyle(AppTheme.code.keyword, "fun ")
        addStyle(AppTheme.code.keyword, "val ")
        addStyle(AppTheme.code.keyword, "var ")
        addStyle(AppTheme.code.keyword, "private ")
        addStyle(AppTheme.code.keyword, "internal ")
        addStyle(AppTheme.code.keyword, "for ")
        addStyle(AppTheme.code.keyword, "expect ")
        addStyle(AppTheme.code.keyword, "actual ")
        addStyle(AppTheme.code.keyword, "import ")
        addStyle(AppTheme.code.keyword, "package ")
        addStyle(AppTheme.code.value, "true")
        addStyle(AppTheme.code.value, "false")
        addStyle(AppTheme.code.value, Regex("[0-9]*"))
        addStyle(AppTheme.code.annotation, Regex("^@[a-zA-Z_]*"))
        addStyle(AppTheme.code.comment, Regex("^\\s*//.*"))
    }
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, regexp: String) {
    addStyle(style, fromLiteral(regexp))
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, regexp: Regex) {
    for (result in regexp.findAll(toString())) {
        addStyle(style, result.range.first, result.range.last + 1)
    }
}