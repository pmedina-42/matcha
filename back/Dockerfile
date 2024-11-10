FROM maven:3.9.7-eclipse-temurin-22-alpine AS build

WORKDIR /app

COPY backend /app

RUN mvn clean package

FROM eclipse-temurin:22-jre

WORKDIR /app

COPY --from=build /app/target/*dependencies.jar /app/app.jar
COPY ../.env /app/.env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

