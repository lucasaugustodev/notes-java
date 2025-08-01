# Multi-stage build for Notes App

# Stage 1: Build the application
FROM maven:3.8.6-openjdk-11-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Create a non-root user
RUN groupadd -r notesapp && useradd -r -g notesapp notesapp

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Create directory for database
RUN mkdir -p /app/data && chown -R notesapp:notesapp /app

# Switch to non-root user
USER notesapp

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/login || exit 1

# Set JVM options for container
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
