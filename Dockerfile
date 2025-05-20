# Gunakan Maven untuk membangun project (builder stage)
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Salin file pom dan source code
COPY pom.xml .
COPY src ./src

# Build file JAR
RUN mvn clean package -DskipTests

# -------------------------

# Stage runtime: base image ringan untuk menjalankan jar
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Salin file jar dari builder
COPY --from=builder /app/target/bitcoin-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]
