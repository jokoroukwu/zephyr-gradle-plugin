package io.github.jokoroukwu.zephyrgradleplugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.io.File
import java.net.URL
import java.time.ZoneId
import java.time.ZoneOffset

const val EXTENSION_DISPLAY_NAME = "zephyr"

open class ZephyrPluginConfiguration(project: Project) {

    var projectKey: String? = null

    var timeZone: ZoneId = ZoneOffset.UTC

    var jiraURL: URL? = null

    var userName: String? = null

    var password: String? = null

    var resultsOutputDir: File? = null


    internal val projectKeyProvider: Provider<String> =
        project.provider { projectKey ?: throw ZephyrPluginConfigurationException.onMissingProperty("projectKey") }

    internal val timeZoneProvider: Provider<ZoneId> = project.provider { timeZone }

    internal val jiraURLProvider: Provider<URL> =
        project.provider { jiraURL ?: throw ZephyrPluginConfigurationException.onMissingProperty("jiraURL") }

    internal val userNameProvider: Provider<String> =
        project.provider { userName ?: throw ZephyrPluginConfigurationException.onMissingProperty("username") }

    internal val passwordProvider: Provider<String> =
        project.provider { password ?: throw ZephyrPluginConfigurationException.onMissingProperty("password") }

    internal val resultsOutputDirProvider: Provider<File> =
        project.provider {
            resultsOutputDir ?: throw ZephyrPluginConfigurationException.onMissingProperty("resultsOutputDir")
        }

    override fun toString(): String {
        return "${javaClass.simpleName}: { projectKey: '$projectKey', timeZone: '$timeZone', jiraURL: '$jiraURL'," +
                " userName: '$userName'," +
                " password: ${password?.length?.let { "*".repeat(it) }}," +
                " resultsOutputDir: '$resultsOutputDir' }";
    }
}
