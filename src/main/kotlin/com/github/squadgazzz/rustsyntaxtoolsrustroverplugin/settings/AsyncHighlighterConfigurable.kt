package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

class AsyncHighlighterConfigurable : BoundConfigurable("Rust Async Highlighter") {

    override fun createPanel(): DialogPanel = panel {
        val settings = AsyncHighlighterSettings.getInstance()
        group("Gutter Icons") {
            row {
                checkBox("Show hourglass icons for async calls")
                    .bindSelected(settings::showGutterIcons)
            }
        }
        group("Inline Highlighting (border)") {
            row {
                checkBox("Highlight .await expressions")
                    .bindSelected(settings::highlightAwaitExpressions)
            }
            row {
                checkBox("Highlight async function calls")
                    .bindSelected(settings::highlightAsyncFnCalls)
            }
            row {
                checkBox("Highlight spawn calls")
                    .bindSelected(settings::highlightSpawnCalls)
            }
        }
        group("Inlay Hints") {
            row {
                checkBox("Show inlay hints for async calls")
                    .bindSelected(settings::showInlayHints)
            }
        }
    }
}
