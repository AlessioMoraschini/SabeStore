# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Set environment variables
ARG PROJECT_VERSION
ENV PROJECT_VERSION=${PROJECT_VERSION}
ENV JWT_SECRET=${JWT_SECRET}
ENV SPRING_MAIL_PASSWORD=${SPRING_MAIL_PASSWORD}
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# Copy the fat jar to the container
COPY target/*.jar SabeStore-${PROJECT_VERSION}.jar

# Run the jar file
ENTRYPOINT ["sh", "-c", "java -jar SabeStore-${PROJECT_VERSION}.jar"]
