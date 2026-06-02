FROM maven:3.8-openjdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/INSTAproject-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "INSTAproject-0.0.1-SNAPSHOT.jar"]