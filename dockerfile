# Tahap 1: Build aplikasi menggunakan Maven + Java 21
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Salin seluruh project
COPY . .

# Jalankan Maven Wrapper untuk membuat file JAR
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Tahap 2: Menjalankan aplikasi menggunakan Java 21
FROM eclipse-temurin:21-jre

WORKDIR /app

# Ambil file JAR dari tahap build
COPY --from=build /app/target/*.jar app.jar

# Port default Spring Boot
EXPOSE 8080

# Jalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]