FROM ubuntu:latest
LABEL authors="kl204"

FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} deviceServer-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["top", "-b"]
#test00