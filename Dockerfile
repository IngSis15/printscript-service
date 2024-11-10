# Stage 1: Build the application
FROM gradle:8.10-jdk21 AS builder

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

ENV GITHUB_USER=$GITHUB_USERNAME
ENV GITHUB_TOKEN=$GITHUB_TOKEN

# Set the working directory inside the container
WORKDIR /app

# Copy the gradle wrapper and project files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .


# Copy the source files
COPY src src

# Use the GitHub token secret during the build process (e.g., for pulling dependencies from a private repository)
RUN --mount=type=secret,id=github_token,env=GITHUB_TOKEN,required \
    --mount=type=secret,id=github_username,env=GITHUB_USERNAME,required \
    ./gradlew build --no-daemon

# Stage 2: Create the final image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

COPY ./newrelic/newrelic.jar /app/newrelic/newrelic.jar
COPY ./newrelic/newrelic.yml /app/newrelic/newrelic.yml

# Expose the application port
EXPOSE 8080

ENTRYPOINT ["java","-javaagent:/app/newrelic/newrelic.jar","-jar","app.jar"]
