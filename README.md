# native-cli-example
Demonstrates how to produce a native binary using Graal Native Image in conjunction with a Kotlin JVM shadow JAR.

## Getting Started

Requires Gradle (recommend latest, 7.3+).

Install GraalVM 23.1.0+ for your platform: https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-21.3.0. Use `gu` to install `native-image` (`gu install native-image`). Set this as your current Java.

Now you can build an executable native binary with `gradle nativeBuild`. It outputs the binary into the `build/bin` directory. Run it like any other platform binary, ex. `./build/bin/native-cli`:

[![asciicast](https://asciinema.org/a/310867.svg)](https://asciinema.org/a/310867?speed=4&loop=0&autoplay=1)

### Docker Images

To build a Docker image (Debian Linux), build the JAR first: `gradle shadowJar`, and then the docker image: `docker build -tag <your-image-name-here> .` You can now run it:

```
docker run -it --rm <your-image-name-here>
```
