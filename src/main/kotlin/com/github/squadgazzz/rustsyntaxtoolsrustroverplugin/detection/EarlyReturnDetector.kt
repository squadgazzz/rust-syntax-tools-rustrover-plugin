package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection

import com.intellij.psi.PsiElement
import org.rust.lang.core.psi.RsMacroCall
import org.rust.lang.core.psi.RsMethodCallExpr
import org.rust.lang.core.psi.RsRetExpr
import org.rust.lang.core.psi.RsTryExpr

object EarlyReturnDetector {

    private val PANIC_MACROS = setOf("panic", "unreachable", "unimplemented", "todo")

    enum class EarlyReturnType {
        RETURN,
        TRY_OPERATOR,
        UNWRAP_EXPECT,
        PANIC_MACRO,
    }

    data class DetectionResult(
        val type: EarlyReturnType,
        val anchor: PsiElement,
    )

    fun detect(element: PsiElement): DetectionResult? {
        return when {
            isReturnExpr(element) -> DetectionResult(EarlyReturnType.RETURN, element)
            isTryOperator(element) -> {
                val anchor = (element as RsTryExpr).q
                DetectionResult(EarlyReturnType.TRY_OPERATOR, anchor)
            }
            isUnwrapOrExpect(element) -> {
                val anchor = (element as RsMethodCallExpr).methodCall
                DetectionResult(EarlyReturnType.UNWRAP_EXPECT, anchor)
            }
            isPanicMacro(element) -> DetectionResult(EarlyReturnType.PANIC_MACRO, element)
            else -> null
        }
    }

    fun isReturnExpr(element: PsiElement): Boolean {
        return element is RsRetExpr
    }

    fun isTryOperator(element: PsiElement): Boolean {
        return element is RsTryExpr
    }

    fun isUnwrapOrExpect(element: PsiElement): Boolean {
        if (element !is RsMethodCallExpr) return false
        return try {
            val methodName = element.methodCall.referenceName
            methodName == "unwrap" || methodName == "expect"
        } catch (_: Exception) {
            false
        }
    }

    fun isPanicMacro(element: PsiElement): Boolean {
        if (element !is RsMacroCall) return false
        return try {
            val macroName = element.path.referenceName
            macroName in PANIC_MACROS
        } catch (_: Exception) {
            false
        }
    }
}
