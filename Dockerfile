# Build stage
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV PORT=8080

# Expose port
EXPOSE ${PORT}

# Run the application
CMD ["java", "-jar", "-Dserver.port=${PORT}", "app.jar"]
