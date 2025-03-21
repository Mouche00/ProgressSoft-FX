FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ARG JAR_FILE=progresssoft.jar
ENV JAR_FILE=${JAR_FILE}

COPY --from=builder /app/target/*.jar ./${JAR_FILE}

EXPOSE 8761

ENTRYPOINT java -jar ${JAR_FILE}

