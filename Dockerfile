# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the fat jar to the container
COPY target/*.jar SabeStore.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "SabeStore.jar"]