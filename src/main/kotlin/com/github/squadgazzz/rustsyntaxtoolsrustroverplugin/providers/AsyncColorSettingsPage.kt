package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class AsyncColorSettingsPage : ColorSettingsPage {

    private val descriptors = arrayOf(
        AttributesDescriptor("Rust Async Highlighter//Await expression", AsyncAnnotator.ASYNC_AWAIT_KEY),
        AttributesDescriptor("Rust Async Highlighter//Async function call", AsyncAnnotator.ASYNC_FN_CALL_KEY),
        AttributesDescriptor("Rust Async Highlighter//Async block", AsyncAnnotator.ASYNC_BLOCK_KEY),
        AttributesDescriptor("Rust Async Highlighter//Spawn call", AsyncAnnotator.ASYNC_SPAWN_CALL_KEY),
    )

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "Rust Enhanced Syntax Highlighting//Async"
    override fun getIcon(): Icon? = null
    override fun getHighlighter(): SyntaxHighlighter = PlainSyntaxHighlighter()

    override fun getDemoText(): String = """
        async fn fetch_data(url: &str) -> String {
            let response = client.get(url).<await_expr>await</await_expr>;
            response.text().<await_expr>await</await_expr>
        }

        async fn process() {
            let data = <async_fn>fetch_data</async_fn>("https://example.com").await;
            <spawn_call>tokio::spawn</spawn_call>(<async_block>async move</async_block> { handle(data) });
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = mapOf(
        "await_expr" to AsyncAnnotator.ASYNC_AWAIT_KEY,
        "async_fn" to AsyncAnnotator.ASYNC_FN_CALL_KEY,
        "async_block" to AsyncAnnotator.ASYNC_BLOCK_KEY,
        "spawn_call" to AsyncAnnotator.ASYNC_SPAWN_CALL_KEY,
    )
}
