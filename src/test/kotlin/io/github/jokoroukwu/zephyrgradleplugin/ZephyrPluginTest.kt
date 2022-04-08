package io.github.jokoroukwu.zephyrgradleplugin

import org.amshove.kluent.*
import org.gradle.api.Project
import org.gradle.api.internal.tasks.testing.testng.TestNGTestFramework
import org.gradle.api.tasks.testing.testng.TestNGOptions
import org.gradle.testfixtures.ProjectBuilder
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.net.URL

class ZephyrPluginTest {
    private val dependencyVersion = "0.1.1"
    private val testTask = org.gradle.api.tasks.testing.Test::class.java
    private lateinit var project: Project

    @BeforeClass
    fun setUp() {
        project = ProjectBuilder.builder().build()
        with(project) {
            pluginManager.run {
                    apply(ZephyrPlugin::class.java)
                    apply("java")
                }
            tasks.withType(testTask).forEach { it.useTestNG() }

            extensions.getByType(ZephyrPluginConfiguration::class.java).run {
                    projectKey = "ABC"
                    jiraURL = URL("https://some-url.com")
                    userName = "myUser"
                    password = "myPass"
                    resultsOutputDir = projectDir
                }
            evaluationDependsOn(":")
        }
    }

    @Test
    fun `should apply publish results task`() {
        project.tasks.withType(PublishResultsTask::class.java) shouldHaveSize 1
    }

    @Test
    fun `should contain expected testNG listener`() {
        project.tasks.withType(testTask)
            .map(org.gradle.api.tasks.testing.Test::getTestFramework)
            .map { it as TestNGTestFramework }
            .map(TestNGTestFramework::getOptions)
            .flatMap(TestNGOptions::getListeners) shouldHaveSize 1 `should contain` "io.github.jokoroukwu.zephyrng.ZephyrJsonReporter"
    }

    @Test
    fun `should contain expected dependency`() {
        val result = project.configurations
            .filter { it.name == "implementation" }
            .flatMap { it.dependencies }
            .map { "${it.group}:${it.name}:${it.version}" }

        with(result) {
            shouldHaveSize(1)
            `should contain`("io.github.jokoroukwu:zephyrng:$dependencyVersion")
        }
    }

    @Test
    fun `should contain expected extension`() {
        project.extensions.getByType(ZephyrPluginConfiguration::class.java) shouldNotBe null
    }
}