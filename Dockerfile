FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/beatsaber-bot-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
