FROM openjdk:21-jdk-slim

# Cài Ant
RUN apt-get update && apt-get install -y ant && apt-get clean

WORKDIR /app

# Copy toàn bộ source vào container
COPY . .

# Build project với Ant
RUN ant

# Chạy file JAR trong thư mục dist
CMD ["java", "-jar", "dist/CoffeeManager.jar"]
