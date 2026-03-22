package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class EarlyReturnColorSettingsPage : ColorSettingsPage {

    private val descriptors = arrayOf(
        AttributesDescriptor("Rust Enhanced Syntax Highlighting//Early return", EarlyReturnAnnotator.RETURN_KEY),
        AttributesDescriptor("Rust Enhanced Syntax Highlighting//Try operator (?)", EarlyReturnAnnotator.TRY_OPERATOR_KEY),
        AttributesDescriptor("Rust Enhanced Syntax Highlighting//unwrap/expect", EarlyReturnAnnotator.UNWRAP_EXPECT_KEY),
        AttributesDescriptor("Rust Enhanced Syntax Highlighting//Panic macro", EarlyReturnAnnotator.PANIC_MACRO_KEY),
    )

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "Rust Enhanced Syntax Highlighting//Early Returns"
    override fun getIcon(): Icon? = null
    override fun getHighlighter(): SyntaxHighlighter = PlainSyntaxHighlighter()

    override fun getDemoText(): String = """
        fn process(input: Option<String>) -> Result<i32, Error> {
            let value = input.ok_or(Error::Missing)<try_op>?</try_op>;

            if value.is_empty() {
                <return_stmt>return</return_stmt> Err(Error::Empty);
            }

            let parsed = value.parse::<i32>().<unwrap_call>unwrap</unwrap_call>();
            let checked = validate(parsed).<expect_call>expect</expect_call>("validation failed");

            if checked < 0 {
                <panic_call>panic!</panic_call>("negative value");
            }

            Ok(checked)
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = mapOf(
        "return_stmt" to EarlyReturnAnnotator.RETURN_KEY,
        "try_op" to EarlyReturnAnnotator.TRY_OPERATOR_KEY,
        "unwrap_call" to EarlyReturnAnnotator.UNWRAP_EXPECT_KEY,
        "expect_call" to EarlyReturnAnnotator.UNWRAP_EXPECT_KEY,
        "panic_call" to EarlyReturnAnnotator.PANIC_MACRO_KEY,
    )
}
