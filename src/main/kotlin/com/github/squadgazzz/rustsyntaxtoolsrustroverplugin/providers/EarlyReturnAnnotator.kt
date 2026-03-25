package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.EarlyReturnDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.TestCodeDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement

class EarlyReturnAnnotator : Annotator {

    companion object {
        // Default colors are provided via bundled color scheme XML files.
        val RETURN_KEY = TextAttributesKey.createTextAttributesKey("RUST_EARLY_RETURN")
        val TRY_OPERATOR_KEY = TextAttributesKey.createTextAttributesKey("RUST_TRY_OPERATOR")
        val UNWRAP_EXPECT_KEY = TextAttributesKey.createTextAttributesKey("RUST_UNWRAP_EXPECT")
        val PANIC_MACRO_KEY = TextAttributesKey.createTextAttributesKey("RUST_PANIC_MACRO")
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val settings = AsyncHighlighterSettings.getInstance()
        val detection = EarlyReturnDetector.detect(element) ?: return

        if (!settings.showEarlyReturnsInTests && TestCodeDetector.isInTestCode(element)) return

        val key = when (detection.type) {
            EarlyReturnDetector.EarlyReturnType.RETURN -> {
                if (!settings.highlightReturn) return
                RETURN_KEY
            }
            EarlyReturnDetector.EarlyReturnType.TRY_OPERATOR -> {
                if (!settings.highlightTryOperator) return
                TRY_OPERATOR_KEY
            }
            EarlyReturnDetector.EarlyReturnType.UNWRAP_EXPECT -> {
                if (!settings.highlightUnwrapExpect) return
                UNWRAP_EXPECT_KEY
            }
            EarlyReturnDetector.EarlyReturnType.PANIC_MACRO -> {
                if (!settings.highlightPanicMacros) return
                PANIC_MACRO_KEY
            }
        }

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(detection.anchor)
            .textAttributes(key)
            .create()
    }
}
