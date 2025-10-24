# ============
# 🏗️ STAGE 1 - BUILD THE APP
# ============
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Maven runner + pom
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# settings.xml to download cross-service dependency
COPY settings.xml /

RUN --mount=type=secret,id=github-username,env=GITHUB_USERNAME,required=true \
  --mount=type=secret,id=github-token,env=GITHUB_TOKEN,required=true \
  --mount=type=cache,target=/root/.m2 \
  cp /settings.xml /root/.m2

# Build the Spring Boot application
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \ 
  ./mvnw clean package -DskipTests

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
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
