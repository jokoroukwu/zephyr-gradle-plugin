package io.github.jokoroukwu.zephyrgradleplugin

import io.github.jokoroukwu.zephyrapi.ZephyrClient
import io.github.jokoroukwu.zephyrapi.publication.TestRunBase
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.streams.toList

const val TASK_DISPLAY_NAME = "publishTestResults"

open class PublishResultsTask
@Inject
constructor(private val zephyrClient: ZephyrClient) : DefaultTask() {
    init {
        group = "zephyr"
    }

    @TaskAction
    fun publishResults() {
        readJsonFiles(project.zephyrConfiguration().resultsOutputDirProvider.get().toPath())
            .apply(::publishFileResults)
            .forEach(Files::deleteIfExists)
    }

    private fun publishFileResults(files: Collection<Path>) {
        files.asSequence()
            .map(Files::readAllLines)
            .map { it.joinToString(separator = "") }
            .flatMap { Json.decodeFromString<List<TestRunBase>>(it).asSequence() }
            .toList()
            .let { zephyrClient.publishTestResults(it, ZephyrConfigurationInternal(project.zephyrConfiguration())) }
    }

    private fun readJsonFiles(root: Path): Collection<Path> =
        Files.walk(root, 1)
            .filter { Files.isRegularFile(it) }
            .filter(::hasJsonExtension)
            .toList()


    private fun hasJsonExtension(file: Path) = file.fileName.toString().endsWith(".json")
}

