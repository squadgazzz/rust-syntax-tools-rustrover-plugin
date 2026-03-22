package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import java.awt.Color
import java.awt.Font

class AsyncAnnotator : Annotator {

    companion object {
        // Use ROUNDED_BOX effect (subtle border) instead of background color.
        // This works well on both light and dark themes. Users can customize
        // via Settings > Editor > Color Scheme > Rust Async Highlighter.
        val ASYNC_AWAIT_KEY = TextAttributesKey.createTextAttributesKey(
            "RUST_ASYNC_AWAIT",
            TextAttributes(null, null, Color(232, 163, 23), EffectType.ROUNDED_BOX, Font.PLAIN),
        )

        val ASYNC_FN_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "RUST_ASYNC_FN_CALL",
            TextAttributes(null, null, Color(100, 180, 100), EffectType.ROUNDED_BOX, Font.PLAIN),
        )

        val ASYNC_SPAWN_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "RUST_ASYNC_SPAWN_CALL",
            TextAttributes(null, null, Color(180, 100, 100), EffectType.ROUNDED_BOX, Font.PLAIN),
        )
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
