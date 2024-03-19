FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/challenge.jar /app
EXPOSE 8080
CMD ["java", "-jar", "challenge.jar"]