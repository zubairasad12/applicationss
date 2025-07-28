import org.gradle.api.tasks.Delete
import org.gradle.api.file.Directory

plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false  // ✅ updated
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// ✅ NDK version fix (Flutter plugins need NDK 27)
subprojects {
    afterEvaluate {
        extensions.findByName("android")?.let { ext ->
            ext as com.android.build.gradle.BaseExtension
            ext.ndkVersion = "27.0.12077973"
        }
    }
}

// ✅ Custom build directory setup (optional)
val newBuildDir: Directory = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}

subprojects {
    project.evaluationDependsOn(":app")
}

// ✅ Clean task
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
