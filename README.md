# native-cli-example
Demonstrates how to produce a native binary using Graal Native Image in conjunction with a Kotlin JVM shadow JAR.

## Getting Started

Requires Gradle (recommend latest, 6.2+).

Install GraalVM 20.0.0+ for your platform: https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-20.0.0. If necessary, use `gu` to install `native-image` (`gu install native-image`). If you're not on OS X, you'll need to change the location of the `JAVA_HOME` environment variable set in `build.gradle.kts`:

```Kotlin
    register<Exec>("nativeCompile") {
        dependsOn("shadowJar")
        environment("JAVA_HOME", "<path-to-graal-directory>/graalvm-ce-java11-20.0.0/Contents/Home/bin/")
        // ...
    }
```

Now you can build an executable native binary with `gradle nativeCompile`. It outputs the binary into the `build/bin` directory. Run it like any other platform binary, ex. `./build/bin/native-cli`:

[![asciicast](https://asciinema.org/a/310867.svg)](https://asciinema.org/a/310867?speed=4&loop=0&autoplay=1)

### Docker Images

To build a Docker image (Debian Linux), build the JAR first: `gradle shadowJar`, and then the docker image: `docker build -tag <your-image-name-here> .` You can now run it:

```
docker run -it --rm <your-image-name-here>
```
