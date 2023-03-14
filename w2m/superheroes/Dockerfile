FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /superheroes

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn -B package -DskipTests
RUN cp target/superheroes.jar superheroes.jar

FROM openjdk:17-jdk-slim
WORKDIR /superheroes

COPY --from=builder /superheroes/superheroes.jar .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "superheroes.jar"]
