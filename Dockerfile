FROM openjdk:22-jdk-slim

LABEL authors="sebastian_rodriguez"

ARG JAR_FILE=target/etraveligroup-0.0.1.jar

# Previously perform mvn clean package
COPY ${JAR_FILE} app_etraveligroup.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app_etraveligroup.jar"]