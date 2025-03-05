FROM openjdk:21
EXPOSE 8092
ADD target/*.jar inventory.jar
ENTRYPOINT ["java", "-jar", "inventory.jar"]

LABEL authors="shauryadeepsinh.raolji"