# Stage 1: Build the application with Maven Wrapper
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper files and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Normalize Windows CRLF line endings for Linux and grant execute permission
RUN tr -d '\r' < ./mvnw > ./mvnw.sh && mv ./mvnw.sh ./mvnw && chmod +x ./mvnw

# Copy source code and package application
COPY src/ ./src/
RUN ./mvnw clean package -DskipTests && \
    cp $(find target -name "*.jar" ! -name "*original*") app.jar

# Stage 2: Lightweight runtime environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=builder /app/app.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
