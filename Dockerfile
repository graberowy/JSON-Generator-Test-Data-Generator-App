FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY target/json-generator-1.0-SNAPSHOT-jar-with-dependencies.jar /app/json-generator.jar
COPY frontend /app/frontend
EXPOSE 8080
CMD ["java", "-jar", "/app/json-generator.jar"]