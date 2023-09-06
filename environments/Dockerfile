# Stage 1: Compile the application
FROM maven:3.8.5-openjdk-17 AS build

ENV MAVEN_OPTS="-Xmx512m"

WORKDIR /app

COPY pom.xml .
COPY src ./src

# LABEL for the compile stage
LABEL description="Open Management Application - Build Stage"
LABEL maintainer="JFECM<jfecm.dev@gmail.com>"

# Skip tests during build
RUN mvn clean package -DskipTests

# Stage 2: Create a smaller image for runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only the resulting JAR artifact from stage 11
COPY --from=build /app/target/*.jar app.jar

# LABEL for the execution stage
LABEL description="Open Management Application - Runtime Stage"
LABEL maintainer="JFECM<jfecm.dev@gmail.com>"

# CMD to run the application
CMD ["java", "-jar", "app.jar"]