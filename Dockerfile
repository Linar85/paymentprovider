FROM openjdk:20-ea-1-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:20-ea-1-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/paymentprovider-0.0.1.jar .
COPY docker-startup.sh .
RUN chmod +x docker-startup.sh
EXPOSE 8080
CMD ["./docker-startup.sh"]