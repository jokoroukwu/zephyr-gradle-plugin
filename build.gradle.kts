import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

group = "io.github.jokoroukwu"
version = "1.1"


plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}


gradlePlugin {
    plugins {
        create("zephyr-gradle-plugin") {
            displayName = "zephyr-gradle-plugin"
            id = "$group.zephyr-gradle-plugin"
            implementationClass = "$group.zephyrgradleplugin.ZephyrPlugin"
        }
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib", "1.4.10"))
    implementation("io.github.jokoroukwu:zephyr-api:0.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.4.10")

    testImplementation("org.testng:testng:7.3.0")
    testImplementation ("org.amshove.kluent:kluent:1.68")
}

tasks.test {
    useTestNG()
    testLogging {
        events = events + setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        showExceptions = true
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showStackTraces = true
        showStandardStreams = true
    }
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

