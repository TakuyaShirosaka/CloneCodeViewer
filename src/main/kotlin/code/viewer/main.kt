package code.viewer

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import code.viewer.ui.MainView
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.SwingUtilities.invokeLater


// 起点になるポイント
fun main() {

    //イベントディスパッチスレッド以外のスレッドからGUIの描画処理を行う場合
    //javax.swing:invokeLater() メソッドを使用
    invokeLater {
        // 指定されたコンテンツでウィンドウを開きます。
        Window(
            title = "Clone Code Viewer",
            size = IntSize(1280, 768),
            icon = loadImageResource("ic_launcher.png")
        ) {
            MainView()
        }
    }
}

private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource $path not found" }
    return resource.openStream().use(ImageIO::read)
}