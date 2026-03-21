package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector.AsyncCallType
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.icons.PluginIcons
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement

class AsyncLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>,
    ) {
        if (!AsyncHighlighterSettings.getInstance().showGutterIcons) return

        val seenLines = mutableSetOf<Int>()

        for (element in elements) {
            val callType = AsyncCallDetector.detect(element) ?: continue

            val document = element.containingFile?.viewProvider?.document ?: continue
            val lineNumber = document.getLineNumber(element.textRange.startOffset)
            if (!seenLines.add(lineNumber)) continue

            val tooltip = when (callType) {
                AsyncCallType.AWAIT -> "Await expression"
                AsyncCallType.ASYNC_FN_CALL -> "Async function call"
                AsyncCallType.SPAWN_CALL -> "Spawn call"
            }

            result.add(
                LineMarkerInfo(
                    element,
                    element.textRange,
                    PluginIcons.Hourglass,
                    { tooltip },
                    null,
                    GutterIconRenderer.Alignment.RIGHT,
                    { tooltip },
                )
            )
        }
    }
}
