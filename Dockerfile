FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/Bank-card-API.jar Bank-card-API.jar
ENTRYPOINT ["java", "-jar", "Bank-card-API.jar"]