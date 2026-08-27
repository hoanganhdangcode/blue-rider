FROM gradle:9-jdk21 AS builder

WORKDIR /src
COPY crypto /src/crypto
COPY core-service /src/core-service

WORKDIR /src/core-service
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre

RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /src/core-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
