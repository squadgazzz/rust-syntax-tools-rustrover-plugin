package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AsyncLineMarkerProviderTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    fun testGutterIconsAppearForAwait() {
        myFixture.configureByFile("detection/await_expression.rs")
        val gutters = myFixture.findAllGutters()
        assertTrue(
            "Should have at least one gutter icon for .await expressions, found ${gutters.size}",
            gutters.isNotEmpty(),
        )
    }

    fun testNoGutterIconsForSyncCode() {
        myFixture.configureByText(
            "sync_only.rs",
            """
            fn main() {
                let x = 42;
                println!("{}", x);
            }
            """.trimIndent(),
        )
        val gutters = myFixture.findAllGutters()
        assertTrue(
            "Sync-only code should have no async gutter icons, found ${gutters.size}",
            gutters.isEmpty(),
        )
    }
}
