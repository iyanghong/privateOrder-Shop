FROM openjdk:8
ADD target/shop-1.0-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "/app.jar"]