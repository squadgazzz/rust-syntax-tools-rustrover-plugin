package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import java.awt.Color
import java.awt.Font

class AsyncAnnotator : Annotator {

    companion object {
        val ASYNC_AWAIT_KEY = TextAttributesKey.createTextAttributesKey(
            "RUST_ASYNC_AWAIT",
            TextAttributes(null, Color(252, 245, 225), null, null, Font.PLAIN),
        )

        val ASYNC_FN_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "RUST_ASYNC_FN_CALL",
            TextAttributes(null, Color(252, 248, 235), null, null, Font.PLAIN),
        )

        val ASYNC_SPAWN_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "RUST_ASYNC_SPAWN_CALL",
            TextAttributes(null, Color(252, 242, 215), null, null, Font.PLAIN),
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (!AsyncHighlighterSettings.getInstance().showInlineHighlighting) return

        val callType = AsyncCallDetector.detect(element) ?: return

        val key = when (callType) {
            AsyncCallDetector.AsyncCallType.AWAIT -> ASYNC_AWAIT_KEY
            AsyncCallDetector.AsyncCallType.ASYNC_FN_CALL -> ASYNC_FN_CALL_KEY
            AsyncCallDetector.AsyncCallType.SPAWN_CALL -> ASYNC_SPAWN_CALL_KEY
        }

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(key)
            .create()
    }
}
