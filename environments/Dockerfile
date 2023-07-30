# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# For run mvn dependency
COPY ../.mvn ./.mvn
COPY ../mvnw ../pom.xml ./

# Install dependencies.
RUN ./mvnw dependency:resolve

# Add source code into the image
COPY ../src ./src

# Run
CMD ["./mvnw", "spring-boot:run"]