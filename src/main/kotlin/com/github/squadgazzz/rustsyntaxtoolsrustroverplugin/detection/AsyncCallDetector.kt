package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection

import com.intellij.psi.PsiElement
import org.rust.lang.core.psi.RsBlockExpr
import org.rust.lang.core.psi.RsCallExpr
import org.rust.lang.core.psi.RsFieldLookupExpr
import org.rust.lang.core.psi.RsFunction
import org.rust.lang.core.psi.RsMethodCallExpr
import org.rust.lang.core.psi.RsPathExpr
import org.rust.lang.core.psi.ext.isAsync
import org.rust.lang.core.psi.ext.qualifiedName

object AsyncCallDetector {

    // Include both re-export paths (tokio::spawn) and actual definition paths
    // (tokio::task::spawn) since PSI resolution follows to the real definition.
    private val SPAWN_FUNCTIONS = setOf(
        "tokio::spawn",
        "tokio::spawn_blocking",
        "tokio::task::spawn",
        "tokio::task::spawn_blocking",
        "tokio::task::spawn_local",
        "async_std::task::spawn",
        "async_std::task::spawn_local",
        "async_std::task::spawn_blocking",
    )

    enum class AsyncCallType {
        AWAIT,
        ASYNC_FN_CALL,
        ASYNC_BLOCK,
        SPAWN_CALL,
    }

    /**
     * Result of detection, including an anchor element for positioning.
     * For .await, the anchor is the fieldLookup child (the `.await` token),
     * not the entire RsFieldLookupExpr which spans the whole receiver chain.
     */
    data class DetectionResult(
        val type: AsyncCallType,
        val anchor: PsiElement,
    )

    fun detect(element: PsiElement): DetectionResult? {
        return when {
            isAwaitExpression(element) -> {
                val anchor = (element as RsFieldLookupExpr).fieldLookup
                DetectionResult(AsyncCallType.AWAIT, anchor)
            }
            isAsyncBlock(element) -> {
                // Anchor on the `async` keyword, not the entire block
                DetectionResult(AsyncCallType.ASYNC_BLOCK, element)
            }
            isSpawnCall(element) -> {
                val anchor = (element as RsCallExpr).expr ?: element
                DetectionResult(AsyncCallType.SPAWN_CALL, anchor)
            }
            isAsyncFnCall(element) -> {
                val anchor = when (element) {
                    is RsMethodCallExpr -> element.methodCall
                    is RsCallExpr -> element.expr ?: element
                    else -> element
                }
                DetectionResult(AsyncCallType.ASYNC_FN_CALL, anchor)
            }
            else -> null
        }
    }

    fun isAwaitExpression(element: PsiElement): Boolean {
        // .await in Rust PSI is represented as RsFieldLookupExpr
        // whose RsFieldLookup child has text "await"
        if (element !is RsFieldLookupExpr) return false
        return try {
            element.fieldLookup.isAsync
        } catch (_: Exception) {
            false
        }
    }

    fun isAsyncBlock(element: PsiElement): Boolean {
        if (element !is RsBlockExpr) return false
        return try {
            element.isAsync
        } catch (_: Exception) {
            false
        }
    }

    fun isAsyncFnCall(element: PsiElement): Boolean {
        return try {
            when (element) {
                is RsCallExpr -> isAsyncCallExpr(element)
                is RsMethodCallExpr -> isAsyncMethodCallExpr(element)
                else -> false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun isSpawnCall(element: PsiElement): Boolean {
        if (element !is RsCallExpr) return false
        return try {
            val pathExpr = element.expr as? RsPathExpr ?: return false
            val resolved = pathExpr.path.reference?.resolve() as? RsFunction ?: return false
            resolved.qualifiedName in SPAWN_FUNCTIONS
        } catch (_: Exception) {
            false
        }
    }

    private fun isAsyncCallExpr(callExpr: RsCallExpr): Boolean {
        val pathExpr = callExpr.expr as? RsPathExpr ?: return false
        val resolved = pathExpr.path.reference?.resolve() as? RsFunction ?: return false
        return resolved.isAsync
    }

    private fun isAsyncMethodCallExpr(methodCallExpr: RsMethodCallExpr): Boolean {
        val resolved = methodCallExpr.methodCall.reference.resolve() as? RsFunction ?: return false
        return resolved.isAsync
    }
}
