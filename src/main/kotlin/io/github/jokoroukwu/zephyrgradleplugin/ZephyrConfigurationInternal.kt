package io.github.jokoroukwu.zephyrgradleplugin

import io.github.jokoroukwu.zephyrapi.config.ZephyrConfig
import java.net.URL
import java.time.ZoneId

internal class ZephyrConfigurationInternal(zephyrPluginConfiguration: ZephyrPluginConfiguration) :
    ZephyrConfig {

    override val jiraUrl: URL = zephyrPluginConfiguration.jiraURLProvider.get()

    override val projectKey: String = zephyrPluginConfiguration.projectKeyProvider.get()

    override val timeZone: ZoneId = zephyrPluginConfiguration.timeZoneProvider.get()

    override val username: String = zephyrPluginConfiguration.userNameProvider.get()

    override val password: String = zephyrPluginConfiguration.passwordProvider.get()

    override fun toString(): String =
        "${javaClass.simpleName}: { projectKey: '$projectKey', timeZone: '$timeZone', jiraURL: '$jiraUrl'," +
                " userName: '$username'," +
                " password: ${"*".repeat(password.length)} }"

}