# --------- BUILD STAGE ---------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom and sources
COPY pom.xml .
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# --------- RUNTIME STAGE ---------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# ⬇️ change jar name if yours is different
COPY --from=build /app/target/library-management-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
