FROM gradle:8.7-jdk17 AS build
WORKDIR /workspace
COPY . .


RUN chmod +x ./gradlew


RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
