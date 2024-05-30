FROM ubuntu:latest
LABEL authors="kl204"

FROM openjdk:17
COPY build/libs/*.jar /deviceServer-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/deviceServer-0.0.1-SNAPSHOT.jar"]

ENTRYPOINT ["top", "-b"]
#test00