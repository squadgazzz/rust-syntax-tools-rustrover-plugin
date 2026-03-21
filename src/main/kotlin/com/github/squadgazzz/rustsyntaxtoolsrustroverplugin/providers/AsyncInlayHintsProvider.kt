package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.providers

import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.detection.AsyncCallDetector.AsyncCallType
import com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings.AsyncHighlighterSettings
import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.InlayPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

@Suppress("UnstableApiUsage")
class AsyncInlayHintsProvider : InlayHintsProvider<NoSettings> {

    override val key: SettingsKey<NoSettings> =
        SettingsKey("rust.async.highlighter.inlay")

    override val name: String = "Rust Async Highlighter"

    override val previewText: String = """
        async fn fetch_data() -> String {
            let result = client.get(url).await;
            tokio::spawn(async { process(result) });
            result
        }
    """.trimIndent()

    override fun createSettings(): NoSettings = NoSettings()

    override fun createConfigurable(settings: NoSettings): ImmediateConfigurable =
        object : ImmediateConfigurable {
            override fun createComponent(listener: ChangeListener) =
                javax.swing.JPanel()
        }

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: NoSettings,
        sink: InlayHintsSink,
    ): InlayHintsCollector? {
        if (!AsyncHighlighterSettings.getInstance().showInlayHints) return null
        return AsyncInlayHintsCollector(editor)
    }

    private class AsyncInlayHintsCollector(editor: Editor) : FactoryInlayHintsCollector(editor) {

        override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
            val detection = AsyncCallDetector.detect(element) ?: return true

            // Skip .await -- the keyword is already visible in source text
            if (detection.type == AsyncCallType.AWAIT) return true

            val label = when (detection.type) {
                AsyncCallType.ASYNC_FN_CALL -> "async"
                AsyncCallType.SPAWN_CALL -> "spawn"
                AsyncCallType.AWAIT -> return true
            }

            val presentation: InlayPresentation = factory.smallText(label)
            @Suppress("DEPRECATION")
            sink.addInlineElement(
                detection.anchor.textRange.endOffset,
                relatesToPrecedingText = true,
                presentation,
            )

            return true
        }
    }
}
