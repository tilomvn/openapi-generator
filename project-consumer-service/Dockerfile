FROM azul/zulu-openjdk-alpine:11-jre
WORKDIR /app
ADD ./build/libs/store-service-1.0.jar /app/store-service.jar
COPY ./src/main/resources/logback.xml /app/logback.xml
ENTRYPOINT ["java", "-jar", "-Dlogging.config=/app/logback.xml", "store-service.jar"]
EXPOSE 8080

