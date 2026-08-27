FROM gradle:9-jdk21 AS builder

WORKDIR /src
COPY crypto /src/crypto
COPY gateway-service /src/gateway-service

WORKDIR /src/gateway-service
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre

RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /src/gateway-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
