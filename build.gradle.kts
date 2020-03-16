import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.70"

    // Apply the application plugin to add support for building a CLI application.
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.github.ajalt:clikt:1.7.0")
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }
    register<Exec>("nativeCompile") {
        dependsOn("shadowJar")
        environment("JAVA_HOME", "/Library/Java/JavaVirtualMachines/graalvm-ce-java11-20.0.0/Contents/Home/bin/")

        val cmdLineTokens = listOf(
            "native-image",
            "--no-server",
            "--report-unsupported-elements-at-runtime",
            "--enable-http",
            "--enable-https",
            "-jar",
            project.buildDir.resolve("libs").resolve("${project.name}-all.jar").toString(),
            project.buildDir.resolve("bin").resolve(project.name).toString()
        )
        commandLine = cmdLineTokens
    }
}

application {
    mainClassName = "io.tednology.cli.AppKt"
}
