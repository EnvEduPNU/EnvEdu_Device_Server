FROM ubuntu:latest
LABEL authors="kl204"

# Use the official OpenJDK image for a base
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file from your host into the container
COPY ./build/libs/*.jar /app/

# Command to run the application
CMD ["java", "-jar", "/app/deviceServer-0.0.1-SNAPSHOT.jar"]

