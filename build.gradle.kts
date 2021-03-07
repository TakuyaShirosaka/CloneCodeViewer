import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jetbrains.compose") version "0.3.0-build135"
}

group = "me.g1023"
version = "1.0"

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    // composeライブラリから依存関係取得
    api(compose.runtime)
    api(compose.foundation)
    api(compose.material)
    api(compose.materialIconsExtended)
    api(compose.desktop.common)
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "code.viewer.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CloneCodeViewer"
        }
    }
}