package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.rust.lang.core.psi.RsFunction
import org.rust.lang.core.psi.RsModItem
import org.rust.lang.core.psi.ext.isTest
import org.rust.lang.core.psi.ext.queryAttributes

object TestCodeDetector {

    fun isInTestCode(element: PsiElement): Boolean {
        return try {
            isInTestsDirectory(element) ||
                isInTestFunction(element) ||
                isInCfgTestModule(element)
        } catch (_: Exception) {
            false
        }
    }

    private fun isInTestsDirectory(element: PsiElement): Boolean {
        val filePath = element.containingFile?.virtualFile?.path ?: return false
        return "/tests/" in filePath
    }

    private fun isInTestFunction(element: PsiElement): Boolean {
        val function = PsiTreeUtil.getParentOfType(element, RsFunction::class.java) ?: return false
        return try {
            function.isTest
        } catch (_: Exception) {
            false
        }
    }

    private fun isInCfgTestModule(element: PsiElement): Boolean {
        var current: PsiElement? = element
        while (current != null) {
            if (current is RsModItem) {
                try {
                    if (current.queryAttributes.hasAttributeWithArg("cfg", "test")) {
                        return true
                    }
                } catch (_: Exception) {
                    // continue walking up
                }
            }
            current = current.parent
        }
        return false
    }
}
