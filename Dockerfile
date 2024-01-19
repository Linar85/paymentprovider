FROM openjdk:17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/build/libs/paymentprovider-0.0.1-SNAPSHOT.jar .
COPY docker-startup.sh .
RUN chmod +x docker-startup.sh
EXPOSE 8080
CMD ["./docker-startup.sh"]