FROM openjdk:17-jdk-slim

WORKDIR /app

COPY admin-backend-1.0.0.jar /app/app.jar
COPY config/application.yml /app/config/application.yml

RUN mkdir -p /app/logs

VOLUME ["/app/logs"]

EXPOSE 18080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.config.location=file:/app/config/application.yml"]
