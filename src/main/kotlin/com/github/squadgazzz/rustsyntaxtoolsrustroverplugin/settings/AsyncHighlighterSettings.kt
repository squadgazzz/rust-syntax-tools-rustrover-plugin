package com.github.squadgazzz.rustsyntaxtoolsrustroverplugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.APP)
@State(name = "AsyncHighlighterSettings", storages = [Storage("AsyncHighlighterSettings.xml")])
class AsyncHighlighterSettings : PersistentStateComponent<AsyncHighlighterSettings> {

    var showGutterIcons: Boolean = true
    var showInlineHighlighting: Boolean = true
    var showInlayHints: Boolean = true

    override fun getState(): AsyncHighlighterSettings = this

    override fun loadState(state: AsyncHighlighterSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): AsyncHighlighterSettings =
            ApplicationManager.getApplication().getService(AsyncHighlighterSettings::class.java)
    }
}
