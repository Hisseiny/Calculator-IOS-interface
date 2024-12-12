// Copyright 2000-2021 JetBrains s.r.o. and contributors.
// Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    val model = CalculatorModel()

    MaterialTheme {
        CalculatorView(model)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Calculator",
        state = WindowState(width = 400.dp, height = 600.dp), // Set initial window size
        resizable = false // Disable resizing
    ) {
        App()
    }
}
