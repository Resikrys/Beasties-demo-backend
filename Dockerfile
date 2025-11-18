# --------------------
# STAGE 1: app Build
# --------------------
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

# --------------------
# STAGE 2: execution Image
# --------------------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]