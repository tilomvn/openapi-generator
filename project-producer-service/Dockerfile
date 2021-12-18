FROM azul/zulu-openjdk-alpine:11-jre
WORKDIR /app
COPY ./build/libs/merchant-service-1.0.jar /app/merchant-service.jar
COPY ./src/main/resources/logback.xml /app/logback.xml
ENTRYPOINT ["java", "-jar", "-Dlogging.config=/app/logback.xml", "merchant-service.jar"]
EXPOSE 8080

