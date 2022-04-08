package io.github.jokoroukwu.zephyrgradleplugin

import org.gradle.api.Project

fun Project.zephyrConfiguration(): ZephyrPluginConfiguration =
    extensions.getByType(ZephyrPluginConfiguration::class.java)

