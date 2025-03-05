FROM openjdk:21
EXPOSE 8090
ADD target/*.jar product.jar
ENTRYPOINT ["java", "-jar", "product.jar"]

LABEL authors="shauryadeepsinh.raolji"