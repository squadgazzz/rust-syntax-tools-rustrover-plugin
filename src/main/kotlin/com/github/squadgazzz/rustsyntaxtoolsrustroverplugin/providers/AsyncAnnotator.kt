package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement

class AsyncAnnotator : Annotator {

    companion object {
        // Default colors are provided via bundled color scheme XML files.
        // Users can customize via Settings > Editor > Color Scheme > Rust Enhanced Syntax Highlighting.
        val ASYNC_AWAIT_KEY = TextAttributesKey.createTextAttributesKey("RUST_ASYNC_AWAIT")
        val ASYNC_FN_CALL_KEY = TextAttributesKey.createTextAttributesKey("RUST_ASYNC_FN_CALL")
        val ASYNC_BLOCK_KEY = TextAttributesKey.createTextAttributesKey("RUST_ASYNC_BLOCK")
        val ASYNC_SPAWN_CALL_KEY = TextAttributesKey.createTextAttributesKey("RUST_ASYNC_SPAWN_CALL")
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val settings = AsyncHighlighterSettings.getInstance()
        val detection = AsyncCallDetector.detect(element) ?: return

        val key = when (detection.type) {
            AsyncCallDetector.AsyncCallType.AWAIT -> {
                if (!settings.highlightAwaitExpressions) return
                ASYNC_AWAIT_KEY
            }
            AsyncCallDetector.AsyncCallType.ASYNC_FN_CALL -> {
                if (!settings.highlightAsyncFnCalls) return
                ASYNC_FN_CALL_KEY
            }
            AsyncCallDetector.AsyncCallType.ASYNC_BLOCK -> {
                if (!settings.highlightAsyncBlocks) return
                ASYNC_BLOCK_KEY
            }
            AsyncCallDetector.AsyncCallType.SPAWN_CALL -> {
                if (!settings.highlightSpawnCalls) return
                ASYNC_SPAWN_CALL_KEY
            }
        }

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(detection.anchor)
            .textAttributes(key)
            .create()
    }
}
