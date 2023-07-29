FROM openjdk:17-alpine

LABEL maintainer="newgr8player <newgr8player@outlook.com>"

ENV JAVA_OPTS=""
ENV WORK_PATH=/src
ENV SERVER_PORT=8080

ARG APP_VERSION

RUN mkdir ${WORK_PATH}

COPY target/pastebin-${APP_VERSION}.jar ${WORK_PATH}/pastebin.jar

WORKDIR ${WORK_PATH}

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["/bin/sh", "-c", "java -Djava.security.egd=file:/dev/./urandom ${JAVA_OPTS} -jar ${WORK_PATH}/pastebin.jar --server.port=${SERVER_PORT}"]