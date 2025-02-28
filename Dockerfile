# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Set environment variables
ENV JWT_SECRET=${JWT_SECRET}

# Copy the fat jar to the container
COPY target/*.jar SabeStore.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "SabeStore.jar"]