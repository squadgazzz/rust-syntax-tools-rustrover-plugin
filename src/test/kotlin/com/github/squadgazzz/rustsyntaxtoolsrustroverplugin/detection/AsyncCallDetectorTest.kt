package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AsyncCallDetectorTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    /** Check if Rust PSI is available in the test environment. */
    private fun isRustPsiAvailable(): Boolean {
        val file = myFixture.configureByText("test.rs", "fn main() {}")
        return file::class.simpleName != "PsiPlainTextFileImpl"
    }

    private fun collectDetected(
        fixturePath: String,
        type: AsyncCallDetector.AsyncCallType,
    ): List<PsiElement> {
        val file = myFixture.configureByFile(fixturePath)
        val results = mutableListOf<PsiElement>()
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (AsyncCallDetector.detect(element)?.type == type) {
                    results.add(element)
                }
                super.visitElement(element)
            }
        })
        return results
    }

    fun testDetectsAwaitExpressions() {
        if (!isRustPsiAvailable()) return  // Skip: Rust plugin not loaded in test sandbox
        val awaits = collectDetected("detection/await_expression.rs", AsyncCallDetector.AsyncCallType.AWAIT)
        assertTrue("Should detect at least one .await expression, found ${awaits.size}", awaits.isNotEmpty())
    }

    fun testDetectsAsyncFnCalls() {
        if (!isRustPsiAvailable()) return
        val asyncCalls = collectDetected("detection/async_fn_call.rs", AsyncCallDetector.AsyncCallType.ASYNC_FN_CALL)
        assertTrue("Should detect at least one async fn call, found ${asyncCalls.size}", asyncCalls.isNotEmpty())
    }

    fun testDetectsSpawnCalls() {
        if (!isRustPsiAvailable()) return
        val spawnCalls = collectDetected("detection/spawn_calls.rs", AsyncCallDetector.AsyncCallType.SPAWN_CALL)
        assertTrue("Should detect at least one spawn call, found ${spawnCalls.size}", spawnCalls.isNotEmpty())
    }

    fun testDoesNotDetectSyncFunctions() {
        if (!isRustPsiAvailable()) return
        val file = myFixture.configureByFile("detection/async_fn_call.rs")
        val falsePositives = mutableListOf<PsiElement>()
        file.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                val detection = AsyncCallDetector.detect(element)
                if (detection?.type == AsyncCallDetector.AsyncCallType.ASYNC_FN_CALL
                    && element.text.contains("sync_fn")
                ) {
                    falsePositives.add(element)
                }
                super.visitElement(element)
            }
        })
        assertTrue("sync_fn() should not be detected as async, found ${falsePositives.size}", falsePositives.isEmpty())
    }

    fun testNestedAwaitInClosure() {
        if (!isRustPsiAvailable()) return
        val awaits = collectDetected("detection/edge_cases.rs", AsyncCallDetector.AsyncCallType.AWAIT)
        assertTrue("Should detect .await in nested closures, found ${awaits.size}", awaits.isNotEmpty())
    }
}
