# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:21
WORKDIR /app
EXPOSE 8092
COPY --from=build /app/target/*.jar notification.jar
ENTRYPOINT ["java", "-jar", "notification.jar"]

LABEL authors="shauryadeepsinh.raolji"