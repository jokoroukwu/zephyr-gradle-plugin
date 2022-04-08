# Zephyr-Plugin

This [Gradle](https://gradle.org/) plugin
integrates Zephyr for JIRA API with a specific
Java test framework such as [TestNG](https://testng.org/doc/).
<br>
The plugin simplifies the configuration by adding the corresponding
build script extension and provides a 
separate Gradle task to publish the results.

## Setup

Register the plugin in the ```settings.gradle``` file:

```groovy
pluginManagement {
    plugins {
        id("io.github.jokoroukwu.zephyr-plugin") version("1.1")
    }
}
```

Apply the plugin to your ```build.gradle``` script:

```groovy
plugins {
    id("io.github.jokoroukwu.zephyr-plugin")
}
```

The plugin is configured via the ``zephyr`` extension:

```groovy
zephyr {
    projectKey = "your-project-key"
    jiraURL = uri("http://your-jira-server-url").toURL()
    userName = "jira-username"
    password = "jira-password"
    //  the directory where the test result files wil be saved
    resultsOutputDir = file("/zephyr-results")
}
```

## Usage

The plugin also adds a Gradle task called ```publishTestResults```.

Running ``./gradlew publishTestResults`` command will collect all test results inside ```resultsOutputDir```
and publish them to Zephyr using the API.

## Licence

Copyright 2022 John Okoroukwu

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "
AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.