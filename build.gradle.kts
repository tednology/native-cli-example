import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.nio.file.Paths

plugins {
    kotlin("jvm") version "1.6.0"
    application
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "io.tednology"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt:clikt:2.8.0")
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")

    testImplementation(kotlin("test"))
}

tasks.apply {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    register<Exec>("toolCheck") {
        doFirst {
            val javaHome = Paths.get(System.getenv("JAVA_HOME"))
            val jvmReleaseData = javaHome.resolve("release").toFile().readLines(Charsets.UTF_8).associate { line ->
                val (key, value) = line.split("=", limit = 2).map { it.trim('"') }
                Pair(key, value)
            }
            val jvmVersion = JavaVersion.toVersion(
                jvmReleaseData.getOrElse("JAVA_VERSION") {
                    throw RuntimeException("Require GraalVM JDK. Check JAVA_HOME.")
                }
            )
            if (!jvmVersion.isCompatibleWith(JavaVersion.VERSION_17)) {
                throw RuntimeException("Requires Java 17 or higher.")
            }
            val isGraalVm = jvmReleaseData["GRAALVM_VERSION"]
            if (isGraalVm.isNullOrBlank()) {
                throw RuntimeException("Require GraalVM JDK. Check JAVA_HOME.")
            }
            val nativeImageInstalled = javaHome.resolve("bin").resolve("native-image").toFile().exists()
            if (!nativeImageInstalled) {
                throw RuntimeException("Requires `native-image` installed. Run `gu install native-image`.")
            }
        }

        commandLine = listOf("native-image", "--version")
        standardOutput = ByteArrayOutputStream()
        doLast {
            val output = standardOutput.toString().trim()
            if (!output.contains("Java 17") && !output.contains("GraalVM")) {
                throw RuntimeException("""
                    Requires a Java 17 compatible GraalVM installation with 
                    `native-image`; check JAVA_HOME environment variable.
                    """.trimIndent())
            }
            logger.quiet(output)
        }
    }

    register<Exec>("buildNative") {
        dependsOn("toolCheck")

        val jarPath = project.buildDir.resolve("libs").resolve("${project.name}-$version-all.jar").toString()
        val binaryPath = project.buildDir.resolve("bin").resolve(project.name).toString()

        commandLine = listOf(
            "native-image",
            "-H:+ReportExceptionStackTraces",
            "--report-unsupported-elements-at-runtime",
            "--enable-http",
            "--enable-https",
            "-jar",
            jarPath,
            binaryPath,
        )
    }
}

application {
    mainClass.set("io.tednology.GreeterKt")
}
