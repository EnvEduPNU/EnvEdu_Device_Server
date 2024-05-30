FROM ubuntu:latest
LABEL authors="kl204"

FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["top", "-b"]
#test00