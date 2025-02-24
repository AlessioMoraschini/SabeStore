# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the fat jar to the container
COPY target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-cp", "app.jar", "com.am.design.development.AmDesignDevelopmentApplication"]