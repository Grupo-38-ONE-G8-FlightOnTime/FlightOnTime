FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY backend/pom.xml /app/pom.xml
COPY backend/src /app/src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV DATASCIENCE_BASE_URL=http://microservice:8000
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

