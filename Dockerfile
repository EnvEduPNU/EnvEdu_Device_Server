FROM openjdk:17-slim

WORKDIR /app

LABEL authors="kl204"

COPY build/libs/*.jar /app/

CMD ["java", "-jar", "/app/dynamodbTest-0.0.1-SNAPSHOT.jar"]

