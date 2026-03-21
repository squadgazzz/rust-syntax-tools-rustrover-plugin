package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

class AsyncHighlighterConfigurable : BoundConfigurable("Rust Async Highlighter") {

    override fun createPanel(): DialogPanel = panel {
        val settings = AsyncHighlighterSettings.getInstance()
        group("Async Call Visibility") {
            row {
                checkBox("Show gutter icons (hourglass) for async calls")
                    .bindSelected(settings::showGutterIcons)
            }
            row {
                checkBox("Show inline highlighting for async calls")
                    .bindSelected(settings::showInlineHighlighting)
            }
            row {
                checkBox("Show inlay hints for async calls")
                    .bindSelected(settings::showInlayHints)
            }
        }
    }
}
