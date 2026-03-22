package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector.AsyncCallType
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.codeInsight.hints.declarative.HintFormat
import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class AsyncInlayHintsProvider : InlayHintsProvider {

    override fun createCollector(file: PsiFile, editor: Editor): InlayHintsCollector? {
        if (!AsyncHighlighterSettings.getInstance().showInlayHints) return null
        return Collector()
    }

    private class Collector : SharedBypassCollector {

        override fun collectFromElement(element: PsiElement, sink: InlayTreeSink) {
            val detection = AsyncCallDetector.detect(element) ?: return

            // Skip .await and async blocks -- already visible in source text
            if (detection.type == AsyncCallType.AWAIT) return
            if (detection.type == AsyncCallType.ASYNC_BLOCK) return

            val label = when (detection.type) {
                AsyncCallType.ASYNC_FN_CALL -> "async"
                AsyncCallType.SPAWN_CALL -> "spawn"
                else -> return
            }

            val position = InlineInlayPosition(
                detection.anchor.textRange.endOffset,
                relatedToPrevious = true,
            )

            sink.addPresentation(
                position = position,
                payloads = emptyList(),
                tooltip = label,
                hintFormat = HintFormat.default,
            ) {
                text(label)
            }
        }
    }
}
