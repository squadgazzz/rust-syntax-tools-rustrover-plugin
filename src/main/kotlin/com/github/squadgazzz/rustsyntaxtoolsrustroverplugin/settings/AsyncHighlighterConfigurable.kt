package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel

class AsyncHighlighterConfigurable : BoundConfigurable("Rust Enhanced Syntax Highlighting") {

    override fun createPanel(): DialogPanel = panel {
        val settings = AsyncHighlighterSettings.getInstance()
        group("Gutter Icons (hourglass)") {
            row {
                checkBox(".await expressions")
                    .bindSelected(settings::gutterIconForAwait)
            }
            row {
                checkBox("Async function calls")
                    .bindSelected(settings::gutterIconForAsyncFn)
            }
            row {
                checkBox("async / async move blocks")
                    .bindSelected(settings::gutterIconForAsyncBlock)
            }
            row {
                checkBox("Spawn calls")
                    .bindSelected(settings::gutterIconForSpawn)
            }
        }
        group("Inline Highlighting (border)") {
            row {
                checkBox(".await expressions")
                    .bindSelected(settings::highlightAwaitExpressions)
            }
            row {
                checkBox("Async function calls")
                    .bindSelected(settings::highlightAsyncFnCalls)
            }
            row {
                checkBox("async / async move blocks")
                    .bindSelected(settings::highlightAsyncBlocks)
            }
            row {
                checkBox("Spawn calls")
                    .bindSelected(settings::highlightSpawnCalls)
            }
        }
        group("Inlay Hints") {
            row {
                checkBox("Show inlay hints for async calls")
                    .bindSelected(settings::showInlayHints)
            }
        }
        group("Early Return Gutter Icons (↩)") {
            row {
                checkBox("return statements")
                    .bindSelected(settings::gutterIconForReturn)
            }
            row {
                checkBox("? operator")
                    .bindSelected(settings::gutterIconForTryOperator)
            }
            row {
                checkBox("unwrap() / expect() calls")
                    .bindSelected(settings::gutterIconForUnwrapExpect)
            }
            row {
                checkBox("Panic macros (panic!, unreachable!, unimplemented!, todo!)")
                    .bindSelected(settings::gutterIconForPanicMacros)
            }
        }
        group("Early Return Inline Highlighting (border)") {
            row {
                checkBox("return statements")
                    .bindSelected(settings::highlightReturn)
            }
            row {
                checkBox("? operator")
                    .bindSelected(settings::highlightTryOperator)
            }
            row {
                checkBox("unwrap() / expect() calls")
                    .bindSelected(settings::highlightUnwrapExpect)
            }
            row {
                checkBox("Panic macros (panic!, unreachable!, unimplemented!, todo!)")
                    .bindSelected(settings::highlightPanicMacros)
            }
        }
        group("Test Code") {
            row {
                checkBox("Show early return highlighting in test code")
                    .bindSelected(settings::showEarlyReturnsInTests)
            }
        }
    }
}
