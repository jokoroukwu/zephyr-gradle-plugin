package io.github.jokoroukwu.zephyrgradleplugin

import io.github.jokoroukwu.zephyrapi.ZephyrClient
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.UnknownConfigurationException
import org.gradle.api.internal.tasks.testing.testng.TestNGTestFramework
import org.gradle.api.tasks.testing.Test

const val ZEPHYR_RESULTS_DIR_PROPERTY = "zephyr.results.output.dir"

class ZephyrPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        extensions.create(EXTENSION_DISPLAY_NAME, ZephyrPluginConfiguration::class.java, target)
        tasks.register(TASK_DISPLAY_NAME, PublishResultsTask::class.java, ZephyrClient)
        afterEvaluate(configureZephyrIntegration())
    }

    private fun configureZephyrIntegration() = Action<Project> {
        it.tasks.withType(Test::class.java)
            .takeUnless(Collection<Test>::isEmpty)
            ?.forEach { testTask -> it.configureTestFramework(testTask) }
            ?: it.logger.quiet("'Test' task is not specified: Zephyr integration will not be configured")
    }

    private fun Project.configureTestFramework(testTask: Test) {
        val zephyrConfiguration = zephyrConfiguration()
        logger.debug("Using Zephyr Plugin configuration: $zephyrConfiguration")

        val outputDir = zephyrConfiguration.resultsOutputDirProvider.get().absolutePath
        testTask.systemProperty(ZEPHYR_RESULTS_DIR_PROPERTY, outputDir)
        logger.debug("System property '$ZEPHYR_RESULTS_DIR_PROPERTY' set to '$outputDir'")

        try {
            when (val framework = testTask.testFramework) {
                is TestNGTestFramework -> configureTestNg(framework)
                else -> logger.error("Unsupported test framework: $this: only TestNG framework is supported")
            }
        } catch (e: UnknownConfigurationException) {
            throw UnknownConfigurationException("${e.message} Is 'java' plugin applied?")
        }
    }

    private fun Project.configureTestNg(framework: TestNGTestFramework) {
        logger.debug("Resolved test framework is TestNG")
        framework.options.listeners.add("io.github.jokoroukwu.zephyrng.ZephyrJsonReporter")
        dependencies.add("implementation", "io.github.jokoroukwu:zephyrng:0.1.1")
    }
}


