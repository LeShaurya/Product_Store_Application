FROM openjdk:21
EXPOSE 8093
ADD target/*.jar gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]

LABEL authors="shauryadeepsinh.raolji"