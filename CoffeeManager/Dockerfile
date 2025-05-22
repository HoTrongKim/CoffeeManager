FROM openjdk:21-jdk-slim

# Cài đặt Ant
RUN apt-get update && apt-get install -y ant && apt-get clean

# Tạo thư mục làm việc
WORKDIR /app

# Copy toàn bộ nội dung thư mục CoffeeManager (nơi có build.xml, src, v.v.)
COPY . .

# Build project với Ant
RUN ant

# Chạy file JAR sau khi build
CMD ["java", "-jar", "dist/CoffeeManager.jar"]
