FROM openjdk:8-jre-slim
WORKDIR /home
COPY build/libs .
CMD ["java", "-jar", "dashboard-service.jar"]
