FROM openjdk:8-jdk-alpine
COPY build/libs/*.jar app.jar
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]