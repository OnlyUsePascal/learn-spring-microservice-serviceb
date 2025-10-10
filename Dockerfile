# ============
# 🏗️ STAGE 1 - BUILD THE APP
# ============
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper & pom.xml first (for efficient caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies — leverage Docker layer caching
RUN ./mvnw dependency:go-offline

# Copy the full source code
COPY src ./src

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# ============
# 🚀 STAGE 2 - RUN THE APP
# ============
FROM eclipse-temurin:21-jre-alpine AS runner

# Add a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port (you can override in compose)
EXPOSE 8082

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
