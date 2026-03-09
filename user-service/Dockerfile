# Build stage
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew :user-service:bootJar --no-daemon

# Run stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
