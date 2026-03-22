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
        val settings = AsyncHighlighterSettings.getInstance()
        val seenLines = mutableSetOf<Int>()

        for (element in elements) {
            val detection = AsyncCallDetector.detect(element) ?: continue

            val tooltip = when (detection.type) {
                AsyncCallType.AWAIT -> {
                    if (!settings.gutterIconForAwait) continue
                    "Await expression"
                }
                AsyncCallType.ASYNC_FN_CALL -> {
                    if (!settings.gutterIconForAsyncFn) continue
                    "Async function call"
                }
                AsyncCallType.ASYNC_BLOCK -> {
                    if (!settings.gutterIconForAsyncBlock) continue
                    "Async block"
                }
                AsyncCallType.SPAWN_CALL -> {
                    if (!settings.gutterIconForSpawn) continue
                    "Spawn call"
                }
            }

            val anchor = detection.anchor
            val document = element.containingFile?.viewProvider?.document ?: continue
            val lineNumber = document.getLineNumber(anchor.textRange.startOffset)
            if (!seenLines.add(lineNumber)) continue

            result.add(
                LineMarkerInfo(
                    anchor,
                    anchor.textRange,
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
