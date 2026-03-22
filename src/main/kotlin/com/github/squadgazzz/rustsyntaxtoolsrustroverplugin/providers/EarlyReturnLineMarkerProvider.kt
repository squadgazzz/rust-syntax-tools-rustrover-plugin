package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.EarlyReturnDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.EarlyReturnDetector.EarlyReturnType
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.TestCodeDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.icons.PluginIcons
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement

class EarlyReturnLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>,
    ) {
        val settings = AsyncHighlighterSettings.getInstance()
        val seenLines = mutableSetOf<Int>()

        for (element in elements) {
            val detection = EarlyReturnDetector.detect(element) ?: continue

            if (!settings.showEarlyReturnsInTests && TestCodeDetector.isInTestCode(element)) continue

            val tooltip = when (detection.type) {
                EarlyReturnType.RETURN -> {
                    if (!settings.gutterIconForReturn) continue
                    "Early return"
                }
                EarlyReturnType.TRY_OPERATOR -> {
                    if (!settings.gutterIconForTryOperator) continue
                    "Try operator (?)"
                }
                EarlyReturnType.UNWRAP_EXPECT -> {
                    if (!settings.gutterIconForUnwrapExpect) continue
                    "unwrap/expect (may panic)"
                }
                EarlyReturnType.PANIC_MACRO -> {
                    if (!settings.gutterIconForPanicMacros) continue
                    "Panic macro"
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
                    PluginIcons.ReturnArrow,
                    { tooltip },
                    null,
                    GutterIconRenderer.Alignment.RIGHT,
                    { tooltip },
                )
            )
        }
    }
}
