FROM oracle/graalvm-ce:20.0.0-java11 as compiler

ARG JAR_NAME=native-cli-all.jar
ARG BIN_NAME=native-cli

RUN mkdir -p /graal/src
RUN gu install native-image

WORKDIR /graal/src

COPY build/libs/$JAR_NAME /graal/src

RUN native-image \
    --no-server \
    --report-unsupported-elements-at-runtime \
    --enable-http \
    --enable-https \
    --static \
    -jar \
    $JAR_NAME \
    /graal/bin/$BIN_NAME

RUN ls /graal/src/
RUN ls /graal/bin/

FROM debian:stable-slim
COPY --from=compiler /graal/bin/native-cli /
CMD ["/native-cli"]