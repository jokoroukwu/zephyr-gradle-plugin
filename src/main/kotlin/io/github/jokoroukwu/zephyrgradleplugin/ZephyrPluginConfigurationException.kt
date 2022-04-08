package io.github.jokoroukwu.zephyrgradleplugin

class ZephyrPluginConfigurationException(message: String) : RuntimeException(message) {

    companion object {
        fun onMissingProperty(name: String) =
            ZephyrPluginConfigurationException("Zephyr configuration property '$name' should be configured")
    }
}