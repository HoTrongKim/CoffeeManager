# Chọn JDK image
FROM openjdk:17-jdk-slim

# Tạo thư mục app
WORKDIR /app

# Copy toàn bộ project vào container
COPY . .

# Build với Gradle
RUN ./gradlew build

# Chạy app JAR (đặt đúng tên file jar khi build ra)
CMD ["java", "-jar", "build/libs/java-sqlite-app.jar"]
