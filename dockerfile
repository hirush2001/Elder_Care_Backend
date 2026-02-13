# 1️⃣ Use Java runtime
FROM eclipse-temurin:21-jdk-alpine

# 2️⃣ Set working directory
WORKDIR /app

# 3️⃣ Copy the built JAR into the container
COPY target/*.jar app.jar

# 4️⃣ Expose Spring Boot port
EXPOSE 8080

# 5️⃣ Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
